package kr.co.skoopmedia.testapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.Toast

class BatteryStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) {
            return
        }

        val batteryPercentage = batteryPercent(context)

        val msg = when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> String.format("배터리가 `%s` 이하로 남았습니다.", batteryPercentage)
            Intent.ACTION_POWER_DISCONNECTED -> String.format("현재 단말기는 충전 상태가 아닙니다. `%s`", batteryPercentage)
            Intent.ACTION_POWER_CONNECTED -> String.format("현재 단말기는 충전 상태로 변경 되었습니다. `%s`", batteryPercentage)
            else -> ""
        }

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun batteryPercent(context: Context): String {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.getApplicationContext().registerReceiver(null, intentFilter)
                ?: return "측정 실패"

        val batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val batteryScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val percentage = batteryLevel.toFloat() / batteryScale.toFloat()

        return String.format("%d%%", (percentage * 100).toInt())
    }
}