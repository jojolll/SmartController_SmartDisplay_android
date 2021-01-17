package com.hotmail.or_dvir.easysettings_kx.events;

import com.hotmail.or_dvir.easysettings_kx.pojos.BasicSettingsObject;

/**
 * an event which is sent whenever a {@link BasicSettingsObject}
 * is clicked
 */
public class BasicSettingsClickEvent
{
	private BasicSettingsObject clickedSettingsObj;

	/**
	 *
	 * @param clickedSettingsObj the {@link BasicSettingsObject} that was clicked
	 */
	public BasicSettingsClickEvent(BasicSettingsObject clickedSettingsObj)
	{
		this.clickedSettingsObj = clickedSettingsObj;
	}

	/**
	 *
	 * @return the {@link BasicSettingsObject} that was clicked
	 */
	public BasicSettingsObject getClickedSettingsObj()
	{
		return clickedSettingsObj;
	}
}