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
import waveProcess.WaveConstructor;
import waveProcess.WaveFileWriter;
import waveProcess.WavePlayer;

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
	@FXML private Button openFileChooser;

	File file;

	// 各コントロール初期化
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 波形選択ラジオボタンにユーザーデータを登録
		sine.setUserData("sine");
		square.setUserData("square");
		triangle.setUserData("triangle");
		pTriangle.setUserData("pTriangle");
		saw.setUserData("saw");
		pSaw.setUserData("pSaw");
		whiteNoise.setUserData("whiteNoise");
		FCNoise.setUserData("FCNoise");
		// FCノイズパターン周期選択ComboBoxの初期値設定
		FCNoiseFreq.getSelectionModel().selectFirst();
		// 波形選択ラジオボタンのグループ(waveType)に選択時のイベントリスナーを追加
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
		// 音高スライダーが動かされた時に現在の音高を表示
		toneSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						int toneValue = newValue.intValue() + 57;
						labelTone.setText(toneNumtoName(toneValue % 12) + toneValue / 12);
					}
				});
		// 長さスライダーが動かされた時に現在の長さを表示
		lengthSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						labelLength.setText(String.format("%.1f", newValue));
					}
				});
		// 音量スライダーが動かされた時に現在の音量を表示
		ampSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue){
						int percent = (int)(newValue.doubleValue() * 100);
						labelAmp.setText(String.format("%d", percent));
					}
					});

	}

	// 再生ボタンが押されたときの処理
	@FXML
	private void onPlayButtonClicked(ActionEvent e){
		InputWaveProperty property = buildInputWaveProperty();

		WavePlayer.play(WaveConstructor.construct(property));

	}

	// 参照ボタンが押されたときの処理
	@FXML
	private void onOpenFileChooserButtonClicked(ActionEvent e){
		FileChooser chooser = new FileChooser();
		chooser.setTitle("名前をつけて保存");
		chooser.setInitialFileName(
				toggleWaveType.getSelectedToggle().getUserData().toString() + ".wav");
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("WAVファイル", "*.wav"),
				new FileChooser.ExtensionFilter("すべてのファイル", "*.*"));
		file = chooser.showSaveDialog(new Stage());

		if(file != null){
			InputWaveProperty property = buildInputWaveProperty();
			WaveFileWriter.write(WaveConstructor.construct(property), file);
			savePath.setText(file.getAbsolutePath());
		}
	}

	// 現在の選択された波形のプロパティをまとめる
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
	// 音高を表す数を音名に変換
	private String toneNumtoName(int num) {
		return ToneName.TONE_NAME_MAP.get(num);
	}
}
