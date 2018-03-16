package philip.com.wordmeetsdaum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button button = findViewById(R.id.button_insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edittext_input);
                String words = editText.getText().toString();

                if (!TextUtils.isEmpty(words)) {
                    CheckBox checkBox = findViewById(R.id.checkbox_my);

                    Intent intent = new Intent();
                    intent.putExtra(Constant.EXTRA_INPUT_WORDS, words);
                    intent.putExtra(Constant.EXTRA_ADD_TO_MYWORDS, checkBox.isChecked());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
