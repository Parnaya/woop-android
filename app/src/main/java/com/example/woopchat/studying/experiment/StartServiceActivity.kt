package com.example.woopchat.studying.experiment

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.*
import android.content.Intent.EXTRA_INTENT
import android.os.*
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.woopchat.IExperimentAidl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

class StartServiceActivity: AppCompatActivity() {

    private var proxy: IExperimentAidl? = null

    val connection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            proxy = IExperimentAidl.Stub.asInterface(service)
//            TODO("Not yet implemented")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
//            TODO("Not yet implemented")
        }
    }

    val messenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
        Toast.makeText(this, "message comes: $msg", Toast.LENGTH_LONG)
        true
    })

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        setContentView(
//            ActivityServiceBinding().apply {
//
//            }.root
//        )

        val servName = "com.example.woopchat.studying.experiment.StartedService"

        val intent = Intent(this, StartedService::class.java)
        val nested = Intent(this, ChildActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, RequestCodePendingIntent, intent, FLAG_ONE_SHOT or FLAG_IMMUTABLE)
        pendingIntent.send()
        intent.putExtra(EXTRA_INTENT, nested)
        intent.putExtra(PendingIntentKey, pendingIntent)

        intent.extras?.putParcelable("messenger", messenger)
        /** starts bound service */
        bindService(intent, connection, BIND_AUTO_CREATE)
        /** starts started service */
        startService(intent)

        lifecycleScope.launch {
            delay(1000)
            stopService(Intent(this@StartServiceActivity, StartedService::class.java))
        }

//        bindService()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
    }

    companion object {
        const val RequestCodePendingIntent = 0
        const val PendingIntentKey = "PendingIntentKey"
    }
}