package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import waveProcess.WaveUtility;

public class SampleController implements Initializable{
	@FXML private ToggleGroup toggleWaveType;
	@FXML private RadioButton sine;
	@FXML private RadioButton square;
	@FXML private RadioButton triangle;
	@FXML private RadioButton pTriangle;
	@FXML private RadioButton saw;
	@FXML private RadioButton pSaw;
	@FXML private RadioButton whiteNoise;
	@FXML private RadioButton FCNoise;

	@FXML private Label labelPTriStep;
	@FXML private TextField pTriStep;
	@FXML private Label labelPSawStep;
	@FXML private TextField pSawStep;
	@FXML private Label labelFCNoiseFreq;
	@FXML private ComboBox<String> FCNoiseFreq;

	@FXML private TextField baseFreq;
	@FXML private Slider toneSlider;
	@FXML private Label labelTone;
	@FXML private Slider lengthSlider;
	@FXML private Label labelLength;
	@FXML private Slider ampSlider;
	@FXML private Label labelAmp;
	@FXML private Button play;

	@FXML private Label savePath;
	@FXML private Button exportWav;

	/**
	 * Initialize controlls.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/**
		 * Set wave name(String) as UserData to RadioButton(represents wave type).
		 */
		sine.setUserData("sine");
		square.setUserData("square");
		triangle.setUserData("triangle");
		pTriangle.setUserData("pTriangle");
		saw.setUserData("saw");
		pSaw.setUserData("pSaw");
		whiteNoise.setUserData("whiteNoise");
		FCNoise.setUserData("FCNoise");

		/**
		 * Set initial selected value to FCNoiseFreq.
		 */
		FCNoiseFreq.getSelectionModel().selectFirst();

		/**
		 * Add ChangeListener to toggleWaveType.
		 * When "pTriangle" selected, TextField "pTriStep" enabled, and so on.
		 */
		toggleWaveType.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>(){
					@Override
					public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle){
						if(newToggle.equals(pTriangle)){
							pTriStep.setDisable(false);
							pSawStep.setDisable(true);
							FCNoiseFreq.setDisable(true);
							labelPTriStep.setDisable(false);
							labelPSawStep.setDisable(true);
							labelFCNoiseFreq.setDisable(true);
						} else if(newToggle.equals(pSaw)){
							pTriStep.setDisable(true);
							pSawStep.setDisable(false);
							FCNoiseFreq.setDisable(true);
							labelPTriStep.setDisable(true);
							labelPSawStep.setDisable(false);
							labelFCNoiseFreq.setDisable(true);
						} else if(newToggle.equals(FCNoise)){
							pTriStep.setDisable(true);
							pSawStep.setDisable(true);
							FCNoiseFreq.setDisable(false);
							labelPTriStep.setDisable(true);
							labelPSawStep.setDisable(true);
							labelFCNoiseFreq.setDisable(false);
						} else{
							pTriStep.setDisable(true);
							pSawStep.setDisable(true);
							FCNoiseFreq.setDisable(true);
							labelPTriStep.setDisable(true);
							labelPSawStep.setDisable(true);
							labelFCNoiseFreq.setDisable(true);
						}
					}
				});

		/**
		 * Add ChangeListener to Sliders.
		 * Update current value as dragging Sliders.
		 */
		toneSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						// Frequency of wave is calcurated by this : baseFreq * 2^(toneValue/12).
						// To calc tone name and "octave" easily, normalize this value by adding 57
						// toneValue  : A0 -> -48, A#0 -> -47, ...                 ..., A4 -> 0,  ....
						// normalized : A0 -> 9,   A#0 -> 10,  B0 -> 11, C1 -> 12, ..., A4 -> 57, ....
						int normalizedtoneValue = newValue.intValue() + 57;

						// toneValue % 12 : tone name
						// toneValue % 12 : "octave" of tone
						labelTone.setText(toneNumtoName(normalizedtoneValue % 12) + normalizedtoneValue / 12);
					}
				});

		lengthSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						labelLength.setText(String.format("%.1f", newValue));
					}
				});

		ampSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						int percent = (int)(newValue.doubleValue() * 100);
						labelAmp.setText(String.format("%d", percent));
					}
					});

	}

	/**
	 * When "play" button pushed, construct wave and play it.
	 * @param e
	 */
	@FXML
	private void onPlayButtonClicked(ActionEvent e){
		InputWaveProperty property = buildInputWaveProperty();

		WaveUtility.play(WaveUtility.construct(property));
	}

	/**
	 * When "export wav" button pushed, construct wave and write to "*.wav".
	 * @param e
	 */
	@FXML
	private void onExportWavButtonClicked(ActionEvent e){
		File file;
		FileChooser chooser = new FileChooser();
		chooser.setTitle("名前をつけて保存");
		chooser.setInitialFileName(
				toggleWaveType.getSelectedToggle().getUserData().toString() + ".wav");
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("waveファイル", "*.wav"),
				new FileChooser.ExtensionFilter("すべてのファイル", "*.*"));
		file = chooser.showSaveDialog(new Stage());

		if(file != null){
			InputWaveProperty property = buildInputWaveProperty();
			WaveUtility.fileWrite(WaveUtility.construct(property), file);
			savePath.setText(file.getAbsolutePath());
		}
	}

	/**
	 * Build InputWaveProperty from current settings.
	 * @return InputWaveProperty
	 */
	@FXML
	private InputWaveProperty buildInputWaveProperty(){
		String selectedWaveType = toggleWaveType.getSelectedToggle().getUserData().toString();
		int inputStepNum;
		String selectedFCNoiseFreq;

		if(selectedWaveType.equals("pTriangle"))
			inputStepNum = Double.valueOf(pTriStep.getText()).intValue();
		else if(selectedWaveType.equals("pSaw"))
			inputStepNum = Double.valueOf(pSawStep.getText()).intValue();
		else
			inputStepNum = 0;

		if(selectedWaveType.equals("FCNoise"))
			selectedFCNoiseFreq = FCNoiseFreq.getValue();
		else
			selectedFCNoiseFreq = null;

		InputWaveProperty p = new InputWaveProperty(
								  selectedWaveType,
								  Double.valueOf(baseFreq.getText()),
								  (int)toneSlider.getValue(),
								  lengthSlider.getValue(),
								  ampSlider.getValue(),
								  inputStepNum,
								  selectedFCNoiseFreq);
		return p;
	}

	/**
	 * Convert "tone number" (0-11) to "tone name" (C,C#,D,...,B).
	 * @param num tone number
	 * @return tone name
	 */
	private String toneNumtoName(int num) {
		return ToneName.TONE_NAME_MAP.get(num);
	}
}
