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
import javafx.scene.control.ToggleButton;
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

        final Button playButton = new Button("▶");
        
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
                    playButton.setText("▶");
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
        Label windLabel = new Label("Wind");
        radioBar.getChildren().add(windLabel);
        ToggleButton windStartButton = new ToggleButton("▶");
        windStartButton.setUserData(ATConstants.STARTWIND);
        windStartButton.setToggleGroup(windButtonGroup);
        windStartButton.setBorder(getBorder());
        radioBox1.getChildren().add(windStartButton);
        radioBox1.setSpacing(10);
        ToggleButton windStopButton = new ToggleButton("||");
        windStopButton.setUserData(ATConstants.STOPWIND);
        windStopButton.setToggleGroup(windButtonGroup);
        windStopButton.setBorder(getBorder());
        radioBox1.getChildren().add(windStopButton);
        radioBox1.setSpacing(10);
        radioBar.getChildren().add(radioBox1);	
        
        windButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					write(newValue.getUserData().toString());
				}
			}
        	
		});
        
        
        ToggleGroup lightButtonGroup = new ToggleGroup();
        Label lightLabel = new Label("Lighting");
        radioBar.getChildren().add(lightLabel);
        ToggleButton lightStartButton = new ToggleButton("▶");
        lightStartButton.setUserData(ATConstants.STARTLIGHT);
        lightStartButton.setToggleGroup(lightButtonGroup);
        lightStartButton.setBorder(getBorder());
        radioBox2.getChildren().add(lightStartButton);
        radioBox2.setSpacing(10);
        ToggleButton lightStopButton = new ToggleButton("||");
        lightStopButton.setUserData(ATConstants.STOPLIGHT);
        lightStopButton.setToggleGroup(lightButtonGroup);
        lightStopButton.setBorder(getBorder());
        radioBox2.getChildren().add(lightStopButton);
        radioBox2.setSpacing(10);
        radioBar.getChildren().add(radioBox2);
        
        lightButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					write(newValue.getUserData().toString());
				}
			}
        	
		});
        
        Label vibrationLabel = new Label("Vibration");
        radioBar.getChildren().add(vibrationLabel);
        ToggleGroup vibrationButtonGroup = new ToggleGroup();
        ToggleButton vibStartButton = new ToggleButton("▶");
        vibStartButton.setUserData(ATConstants.STARTVIB);
        vibStartButton.setToggleGroup(vibrationButtonGroup);
        vibStartButton.setBorder(getBorder());
        radioBox3.getChildren().add(vibStartButton);
        radioBox3.setSpacing(10);
        ToggleButton vibStopButton = new ToggleButton("||");
        vibStopButton.setUserData(ATConstants.STOPVIB);
        vibStopButton.setToggleGroup(vibrationButtonGroup);
        vibStopButton.setBorder(getBorder());
        radioBox3.getChildren().add(vibStopButton);
        radioBox3.setSpacing(10);
        radioBar.getChildren().add(radioBox3);
        
        vibrationButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					write(newValue.getUserData().toString());
				}
			}
        	
		});
        
        Label scentLabel = new Label("Scent");
        radioBar.getChildren().add(scentLabel);
        ToggleGroup scentButtonGroup = new ToggleGroup();
        ToggleButton scentStartButton = new ToggleButton("▶");
        scentStartButton.setUserData(ATConstants.STARTSCENT);
        scentStartButton.setToggleGroup(scentButtonGroup);
        scentStartButton.setBorder(getBorder());
        radioBox4.getChildren().add(scentStartButton);
        radioBox4.setSpacing(10);
        ToggleButton scentStopButton = new ToggleButton("||");
        scentStopButton.setUserData(ATConstants.STOPSCENT);
        scentStopButton.setToggleGroup(scentButtonGroup);
        scentStopButton.setBorder(getBorder());
        radioBox4.getChildren().add(scentStopButton);
        radioBox4.setSpacing(10);
        radioBar.getChildren().add(radioBox4);
        
        scentButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					write(newValue.getUserData().toString());
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
			out.write(ATConstants.STARTTAG);
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
			if(command.equals(ATConstants.STOPWIND)) {
				out.write(ATConstants.WIND_FALSE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STARTWIND)) {
				out.write(ATConstants.WIND_TRUE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STOPLIGHT)) {
				out.write(ATConstants.LIGHT_FALSE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STARTLIGHT)) {
				out.write(ATConstants.LIGHT_TRUE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STOPVIB)) {
				out.write(ATConstants.VIB_FALSE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STARTVIB)) {
				out.write(ATConstants.VIB_TRUE + pts + ATConstants.CLOSE);
			}  else if(command.equals(ATConstants.STOPSCENT)) {
				out.write(ATConstants.SCENT_FALSE + pts + ATConstants.CLOSE);
			} else if(command.equals(ATConstants.STARTSCENT)) {
				out.write(ATConstants.SCENT_TRUE + pts + ATConstants.CLOSE);
			} else {
				// empty
			}
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	private void saveXML() {
		try {
			out.write(ATConstants.ENDTAG);
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