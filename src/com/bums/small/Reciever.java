package com.bums.small;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Reciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		EventData e = (EventData) intent.getSerializableExtra("the_event");
		Intent service1 = new Intent(context, EventAlarmService.class);
		service1.putExtra("the_event", e);
		context.startService(service1);
	}   
}