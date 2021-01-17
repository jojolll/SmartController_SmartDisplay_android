package com.hotmail.or_dvir.easysettings_dialogs_kx.events;

import com.hotmail.or_dvir.easysettings_dialogs_kx.pojos.EditTextSettingsObject;

/**
 * an event that is sent when the neutral button of an {@link EditTextSettingsObject}
 * is clicked
 */
public class EditTextSettingsNeutralButtonClickedEvent
{
	private EditTextSettingsObject editTextSettingsObj;

	/**
	 *
	 * @param editTextSettingsObj the {@link EditTextSettingsObject} whose neutral button was clicked
	 */
	public EditTextSettingsNeutralButtonClickedEvent(EditTextSettingsObject editTextSettingsObj)
	{
		this.editTextSettingsObj = editTextSettingsObj;
	}

	/**
	 *
	 * @return the {@link EditTextSettingsObject} whose neutral button was clicked
	 */
	public EditTextSettingsObject getEditTextSettingsObj()
	{
		return editTextSettingsObj;
	}
}