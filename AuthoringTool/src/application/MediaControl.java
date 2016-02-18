package application;

/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

import constants.ATConstants;
import constants.ATConstants.ELabel;
import constants.ATConstants.ELightButtons;
import constants.ATConstants.EScentButtons;
import constants.ATConstants.EVibrationButtons;
import constants.ATConstants.EWindButtons;




public class MediaControl extends BorderPane {
	
	

	private BufferedWriter out;
	private double pts;
	
    private MediaPlayer mp;
    private MediaView mediaView;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar1;  //bottomBox
    private HBox mediaBar2;  //TopBox
    private HBox radioBox1;  
    private HBox radioBox2;  
    private HBox radioBox3;
    private HBox radioBox4;
    
    private HBox buttonBox;
    private VBox radioBar;//RightSideBox
    public MediaControl(final MediaPlayer mp ) {
        this.mp = mp;
        setStyle("-fx-background-color: #bfc2c7;");
        mediaView = new MediaView(mp);
        Pane mvPane = new Pane() {
        };
        mvPane.getChildren().add(mediaView);
        mvPane.setStyle("-fx-background-color: black;");
        setCenter(mvPane);

        mediaBar1 = new HBox();
        mediaBar2 = new HBox();
        radioBox1 = new HBox();
        radioBox2 = new HBox();
        radioBox3 = new HBox();
        radioBox4 = new HBox();
        buttonBox = new HBox();
        radioBar = new VBox();
        
        mediaBar1.setAlignment(Pos.CENTER_LEFT);
        mediaBar1.setPadding(new Insets(5, 10, 5, 10));
        mediaBar1.setMinWidth(mp.getMedia().getWidth());
        mediaBar2.setAlignment(Pos.TOP_LEFT);
        //radioBox1.setAlignment(Pos.TOP_LEFT);
        radioBar.setAlignment(Pos.TOP_LEFT);
        
        radioBox1.setPadding(new Insets(10, 10, 20, 10));
        radioBox2.setPadding(new Insets(10, 10, 20, 10));
        radioBox3.setPadding(new Insets(10, 10, 20, 10));
        radioBox4.setPadding(new Insets(10, 10, 20, 10));
        buttonBox.setPadding(new Insets(10, 10, 20, 10));
      
        radioBar.setPadding(new Insets(5, 10, 5, 10));
        radioBar.setMinWidth(300);
        
        
        BorderPane.setAlignment(mediaBar1, Pos.CENTER);
        BorderPane.setAlignment(mediaBar2, Pos.CENTER);
        BorderPane.setAlignment(radioBar, Pos.TOP_LEFT);

        final Button playButton = new Button(">");
        
        //xml 저장
        try {
			this.out = new BufferedWriter(new FileWriter(ATConstants.FILEPATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        //동영상 동작하는 부분설정
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Status status = mp.getStatus();

                if (status == Status.UNKNOWN || status == Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == Status.PAUSED
                        || status == Status.READY
                        || status == Status.STOPPED) {
                    // rewind the movie if we're sitting at the end
                    if (atEndOfMedia) {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                    }
                    mp.play();
                } else {
                    mp.pause();
                }
            }
        });
        
        mp.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            }
        });

        mp.setOnPaused(new Runnable() {
            public void run() {
                System.out.println("onPaused");
                playButton.setText(">");
            }
        });

        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });

        mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
        });
    //create menubar
        
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(mediaBar2.widthProperty());
        mediaBar2.getChildren().add(menuBar);        
     // File menu - new, save, exit
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        
        newMenuItem.setOnAction(new EventHandler<ActionEvent>(){
        	
        	public void handle(ActionEvent t){
        		FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll();
                File selectedFile = fc.showOpenDialog(null); // 다이얼로그 띄움.
                String MEDIA_URL= selectedFile.toURI().toString();
                Media media = new Media (MEDIA_URL);  
                MediaPlayer mediaPlayer = new MediaPlayer(media);             
        	}
        });
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>(){
        	
        	public void handle(ActionEvent s){
        		
        		saveXML();
        	}
        	
        	
        });
        
        
        fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
            new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().addAll(fileMenu);

        mediaBar1.getChildren().add(playButton);
        // Add spacer
        Label spacer = new Label("   ");
        mediaBar1.getChildren().add(spacer);

        // Add Time label
        Label timeLabel = new Label("Time: ");
        mediaBar1.getChildren().add(timeLabel);

        // Add time slider
        timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE); // 슬라이더 이슈 해결 필요
        /*timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });
        */
        mp.currentTimeProperty().addListener(new ChangeListener<Duration>(){
        	public void changed(ObservableValue<? extends Duration> observableValue,Duration duration,Duration current){
        		timeSlider.setValue(current.toSeconds());
            }
        });
        timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent mouseEvnet){
				mp.seek(Duration.seconds(timeSlider.getValue()));
			}
		});
        mediaBar1.getChildren().add(timeSlider);

        // Add Play label
        playTime = new Label();
        playTime.setPrefWidth(130);
        playTime.setMinWidth(50);
        mediaBar1.getChildren().add(playTime);

        // Add the volume label
        Label volumeLabel = new Label("Vol: ");
        mediaBar1.getChildren().add(volumeLabel);

        // Add Volume slider
        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        
        mediaBar1.getChildren().add(volumeSlider);

        
        // create radio button
       
        ToggleGroup windButtonGroup = new ToggleGroup();
        Label windLabel = new Label("바람 효과");
        radioBar.getChildren().add(windLabel);
        for(EWindButtons eButton:EWindButtons.values()){
        	
        	RadioButton button = new RadioButton(eButton.getName());
        	button.setUserData(eButton.getName());
        	button.setToggleGroup(windButtonGroup);
        	button.setBorder(getBorder()); 	
        	radioBox1.getChildren().add(button);
        	radioBox1.setSpacing(10);
        }
        radioBar.getChildren().add(radioBox1);	
        
        windButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue.getUserData().toString().equals("없음")) {
					write("wind0");
				} else if(newValue.getUserData().toString().equals("1")) {
					write("wind1");
				} else if(newValue.getUserData().toString().equals("2")) {
					write("wind2");
				} else if(newValue.getUserData().toString().equals("3")) {
					write("wind3");
				}
			}
        	
		});
        
        
        Label lightLabel = new Label("빛 효과");
        radioBar.getChildren().add(lightLabel);        
        ToggleGroup lightButtonGroup = new ToggleGroup();
        for(ELightButtons eButton:ELightButtons.values()){
        	
        	RadioButton button = new RadioButton(eButton.getName());
        	button.setUserData(eButton.getName());
        	button.setToggleGroup(lightButtonGroup);
        	button.setBorder(getBorder()); 	
        	radioBox2.getChildren().add(button);
        	radioBox2.setSpacing(10);
        }
        radioBar.getChildren().add(radioBox2);	
        
        lightButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue.getUserData().toString().equals("없음")) {
					write("noLight");
				} else if(newValue.getUserData().toString().equals("빨강")) {
					write("red");
				} else if(newValue.getUserData().toString().equals("파랑")) {
					write("blue");
				} else if(newValue.getUserData().toString().equals("초록")) {
					write("green");
				}
			}
        	
		});
        
        Label vibrationLabel = new Label("진동 효과");
        radioBar.getChildren().add(vibrationLabel);
        ToggleGroup vibrationButtonGroup = new ToggleGroup();
        for(EVibrationButtons eButton:EVibrationButtons.values()){
        	
        	RadioButton button = new RadioButton(eButton.getName());
        	button.setUserData(eButton.getName());
        	button.setToggleGroup(vibrationButtonGroup);
        	button.setBorder(getBorder()); 	
        	radioBox3.getChildren().add(button);
        	radioBox3.setSpacing(10);
        }
        radioBar.getChildren().add(radioBox3);
        
        vibrationButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue.getUserData().toString().equals("없음")) {
					write("vib0");
				} else if(newValue.getUserData().toString().equals("1")) {
					write("vib1");
				} else if(newValue.getUserData().toString().equals("2")) {
					write("vib2");
				} else if(newValue.getUserData().toString().equals("3")) {
					write("vib3");
				}
			}
        	
		});
        
        Label scentLabel = new Label("향기 효과");
        radioBar.getChildren().add(scentLabel);
        ToggleGroup scentButtonGroup = new ToggleGroup();
        for(EScentButtons eButton:EScentButtons.values()){
        	
        	RadioButton button = new RadioButton(eButton.getName());
        	button.setUserData(eButton.getName());
        	button.setToggleGroup(scentButtonGroup);
        	button.setBorder(getBorder()); 	
        	radioBox4.getChildren().add(button);
        	radioBox4.setSpacing(10);
        }
        radioBar.getChildren().add(radioBox4);
        
        scentButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue.getUserData().toString().equals("없음")) {
					write("noScent");
				} else if(newValue.getUserData().toString().equals("팝콘")) {
					write("popcorn");
				} else if(newValue.getUserData().toString().equals("화약")) {
					write("gunpowder");
				} else if(newValue.getUserData().toString().equals("꽃")) {
					write("flower");
				}
			}
        	
		});
        
        
        /*
        Button startButton = new Button("start");
        Button endButton = new Button("end");
        buttonBox.getChildren().add(startButton);
        buttonBox.setSpacing(10);
        buttonBox.getChildren().add(endButton);
        radioBar.getChildren().add(buttonBox);
        */
        
        setTop(mediaBar2);
        setBottom(mediaBar1);
        setRight(radioBar);
        
        init();
    }
    
    //write xml
    
    public void init() {
		this.setVisible(true);
		this.pts = 0;
		try {
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<SEM xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:mpeg:mpeg-v:2010:01-SEDL-NS\" xmlns:sev=\"urn:mpeg:mpeg-v:2010:01-SEV-NS\" xmlns:mpeg7=\"urn:mpeg:mpeg7:schema:2004\" xmlns:ct=\"urn:mpeg:mpeg-v:2010:01-CT-NS\" xmlns:si=\"urn:mpeg:mpeg21:2003:01-DIA-XSI-NS\" xsi:schemaLocation=\"urn:mpeg:mpeg-v:2010:01-SEV-NS MPEG-V-SEV.xsd\" si:timeScale=\"1\">\n\n");
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
    
    private void bringPTS() {
    	this.pts = mp.getCurrentTime().toSeconds();
    }
    
    private void write(String command) {
    	bringPTS();
		try {
			if(command.equals("wind0")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind1")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 4.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind2")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 8.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind3")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 12.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("noLight")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("red")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "red" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("blue")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "blue" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("green")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "green" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib0")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib1")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 10.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib2")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 30.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib3")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 50.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("noScent")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("popcorn")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("gunpowder")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("flower")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			}
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	private void saveXML() {
		try {
			out.write("\n</SEM>");
			out.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
    
    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume()
                                * 100));
                    }
                }
            });
        }
    }
  
    
	//시간 넣는건데 안되 고쳐야함
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
}