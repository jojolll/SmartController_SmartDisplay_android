package com.hotmail.or_dvir.easysettings_dialogs_kx.events;

import com.hotmail.or_dvir.easysettings_dialogs_kx.pojos.EditTextSettingsObject;

/**
 * an event that is sent when the value of an {@link EditTextSettingsObject}
 * is changed
 */
public class EditTextSettingsValueChangedEvent
{
	private EditTextSettingsObject editTextSettingsObj;

	/**
	 *
	 * @param editTextSettingsObj the {@link EditTextSettingsObject} whose value was changed
	 */
	public EditTextSettingsValueChangedEvent(EditTextSettingsObject editTextSettingsObj)
	{
		this.editTextSettingsObj = editTextSettingsObj;
	}

	/**
	 *
	 * @return the {@link EditTextSettingsObject} whose value was changed
	 */
	public EditTextSettingsObject getEditTextSettingsObj()
	{
		return editTextSettingsObj;
	}
}