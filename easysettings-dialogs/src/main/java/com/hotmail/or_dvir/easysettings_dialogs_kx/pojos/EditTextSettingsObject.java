package com.hotmail.or_dvir.easysettings_dialogs_kx.pojos;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hotmail.or_dvir.easysettings_dialogs_kx.events.EditTextSettingsValueChangedEvent;
import com.hotmail.or_dvir.easysettings_kx.enums.ESettingsTypes;
import com.hotmail.or_dvir.easysettings_kx.pojos.SettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.R;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * a settings object that when clicked, opens a dialog containing an edit text.
 * see the methods in {@link EditTextSettingsObject.Builder} for available options
 */
public class EditTextSettingsObject extends DialogSettingsObject<EditTextSettingsObject.Builder, String>
        implements Serializable {
    private String hint;
    private int minChars;
    private int maxChars;
    private String prefillText;
    private int inputType;
    private boolean useValueAsPrefillText;
    private MaterialDialog dialog;

    private EditTextSettingsObject(Builder builder) {
        super(builder);
        this.hint = builder.hint;
        this.prefillText = builder.prefillText;
        this.useValueAsPrefillText = builder.useValueAsPrefillText;
        this.minChars = builder.minChars;
        this.maxChars = builder.maxChars;
        this.inputType = builder.inputType;

        //todo if you don't want to use the builder pattern,
        //todo you can also use a regular constructor
    }

    /**
     * @return the hint of the {@link android.widget.EditText}
     */
    public String getHint() {
        return hint;
    }

    /**
     * @return the prefill text of the {@link android.widget.EditText}
     */
    public String getPrefillText() {
        return prefillText;
    }

    @Override
    public int getLayout() {
        //todo in this case i am using the same layout as a basic settings object
        //todo which simply contains a title text view, and a summary text view
        //todo you can put your own custom layout here
        return R.layout.basic_settings_object;
    }

    @Override
    public String checkDataValidity(Context context, SharedPreferences prefs) {
        //in this case, the value saved could be any text...
        //so no validity check is needed.
        //we simply return the previously saved value
        return prefs.getString(getKey(), getDefaultValue());
    }

    @Override
    public String getValueHumanReadable() {
        //in this case, the value saved is itself a string
        return getValue();
    }

    @Override
    public void initializeViews(View root) {
        //todo because we are using the R.layout.layout basic_settings_object,
        //todo we need to call the super method here to initialize the title and summary text views.
        //todo if you are making a completely custom layout here, no need to call the super method
        super.initializeViews(root);

        //todo optional initialization of the summary:
        //todo NOTE: this will override any value set with the setSummary() method
        //todo when building this object
//        tvSummary.setText(previouslySavedValue);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);

                //todo if you'd like, you can also send a custom event here to notify
                //todo the settings activity of the click.
                //todo NOTE: this specific line is only an example and is copy-pasted
                //todo from the class BasicSettingsObject.
                //todo you need to make your own custom event here
//                EventBus.getDefault().post(new BasicSettingsClickEvent(BasicSettingsObject.this));
            }
        });
    }

    /**
     * creates and displays the actual dialog for this {@link EditTextSettingsObject}
     *
     * @param root the root view containing this {@link EditTextSettingsObject}
     */
    private void showDialog(View root) {
        Context context = root.getContext();
        TextView tvSummary = null;

        Integer summaryId = getTextViewSummaryId();
        if (summaryId != null) {
            tvSummary = root.findViewById(summaryId);
        }

        final TextView finalTvSummary = tvSummary;

        MaterialDialog.Builder builder = getBasicMaterialDialogBuilder(context);

        String localHint = null;
        String localPrefillText = null;

        if (getHint().isEmpty() == false) {
            localHint = getHint();
        }

        if (useValueAsPrefillText) {
            localPrefillText = getValueHumanReadable();
        } else if (getPrefillText().isEmpty() == false) {
            localPrefillText = getPrefillText();
        }

        /*InputType.TYPE_CLASS_TEXT*/
        /*TYPE_NUMBER_FLAG_DECIMAL*/
        dialog = builder.inputType(inputType)
                .input(localHint, localPrefillText, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //todo don't forget to save the new value!
                        String newValue = input + "";

                        setValueAndSaveSetting(dialog.getContext(), newValue);
                        String test = getValue();
                        //NOTE:
                        //this if statement MUST come AFTER setting the new value
                        if (finalTvSummary != null &&
                                useValueAsSummary()) {
                            finalTvSummary.setText(getValueHumanReadable());
                        }

                        EventBus.getDefault().post(new EditTextSettingsValueChangedEvent(EditTextSettingsObject.this));
                    }
                }).build();

        AppCompatEditText text = dialog.getView().findViewById(android.R.id.input);

        // disable the ok button if not enought or too much characters
		if ((minChars >= 0) && (text.length() < minChars) || (text.length() > maxChars))
			dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

        text.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) {
                    //do your work here
                    Log.d("sample", "onTextChanged : " + s);
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                Log.d("sample", "afterTextChanged: " + s);
                if ((minChars >= 0) && (s.length() < minChars) || (s.length() > maxChars))
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                else
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
            }
        });


        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "button 1 clicked", Toast.LENGTH_SHORT).show();
                dialog.onClick(view);

                //todo if you'd like, you can also send a custom event here to notify
                //todo the settings activity of the click.
                //todo NOTE: this specific line is only an example and is copy-pasted
                //todo from the class BasicSettingsObject.
                //todo you need to make your own custom event here
            }
        });

        //todo if needed, add more button listeners here
        dialog.show();
    }

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    public static class Builder extends DialogSettingsObject.Builder<Builder, String> {
        private String hint = "";
        private int minChars = -1;
        private int maxChars = 127;
        private String prefillText = "";
        private int inputType = InputType.TYPE_CLASS_TEXT;
        private boolean useValueAsPrefillText = false;

        /**
         * @param key             the key for this {@link EditTextSettingsObject}
         *                        to be saved in the apps' {@link SharedPreferences}
         * @param title           the title for this {@link EditTextSettingsObject}
         * @param defaultValue    the default value for this {@link EditTextSettingsObject}
         * @param positiveBtnText the text to display for the positive button of the dialog
         */
        public Builder(String key,
                       String title,
                       String defaultValue,
                       String positiveBtnText) {
            //todo don't forget to pass your own id's here!
            super(key,
                    title,
                    defaultValue,
                    R.id.textView_basicSettingsObject_title,
                    R.id.textView_basicSettingsObject_summary,
                    ESettingsTypes.STRING,
                    R.id.imageView_basicSettingsObject_icon);

            setPositiveBtnText(positiveBtnText);
        }

        public String getHint() {
            return hint;
        }

        public String getPrefillText() {
            return prefillText;
        }

        /**
         * sets the hint to be used in the dialogs' {@link android.widget.EditText}
         *
         * @param hint
         */
        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        /**
         * sets the xxxxxxxxxxx' {@link android.widget.EditText}
         *
         * @param value
         */
        public Builder setMinCharacters(int value) {
            this.minChars = value;
            return this;
        }

        /**
         * sets the xxxxxxxxxxx' {@link android.widget.EditText}
         *
         * @param value
         */
        public Builder setMaxCharacters(int value) {
            this.maxChars = value;
            return this;
        }

        /**
         * sets the prefill text to be used in the dialogs' {@link android.widget.EditText}
         *
         * @param prefillText
         */
        public Builder setPrefillText(String prefillText) {
            this.prefillText = prefillText;
            return this;
        }


        /**
         * sets the input type
         */
        public Builder setInputType(int value) {
            this.inputType = value;
            return this;
        }


        /**
         * if this method is called, the value of this {@link EditTextSettingsObject}
         * will be used as the pre-fill text of the {@link android.widget.EditText} inside the dialog.
         * this method overrides the value set in {@link SettingsObject.Builder#setUseValueAsSummary()}
         *
         * @return
         */
        public Builder setUseValueAsPrefillText() {
            this.useValueAsPrefillText = true;
            return this;
        }


        @Override
        public EditTextSettingsObject build() {
            return new EditTextSettingsObject(this);
        }
    }
}
