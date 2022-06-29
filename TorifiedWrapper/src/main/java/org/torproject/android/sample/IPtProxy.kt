    package org.torproject.android.sample;

    import IPtProxy.IPtProxy
    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import java.io.File

    class MainActivity : AppCompatActivity() {

        private var count: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            title = "App B"

            val stateLocation = File(cacheDir, "pt")
            if (!stateLocation.exists()) stateLocation.mkdir()

            IPtProxy.setStateLocation(stateLocation.absolutePath)

            // android activity config
            val start = findViewById<Button>(R.id.start)
            val stop = findViewById<Button>(R.id.stop)
            val label = findViewById<TextView>(R.id.label)

            // snowflake proxy config
            val capacity : Long = 0
            // all String args are null to use defaults included in IPtProxy
            val broker = null
            val relay = null
            val stun = null
            val natProbe = null
            val logFile = null
            val keepLocalAddresses = false
            val unsafeLogging = true

            start.setOnClickListener {
                IPtProxy.startSnowflakeProxy(capacity, broker, relay, stun, natProbe, logFile, keepLocalAddresses, unsafeLogging) {
                    runOnUiThread {
                        count++
                        Toast.makeText(this, "Client connected", Toast.LENGTH_LONG).show()
                        label.text = "Connected Clients:$count"
                    }
                }
            }

            stop.setOnClickListener {
                runOnUiThread {
                    IPtProxy.stopSnowflakeProxy()
                }
            }

            val startObfs4 = findViewById<Button>(R.id.startObfs)
            val stopObfs4 = findViewById<Button>(R.id.stopObfs)

            startObfs4.setOnClickListener {
                val obfs4Port = IPtProxy.startObfs4Proxy("DEBUG", false, false, null)
                startObfs4.text = "obfs4 running on Port $obfs4Port"
            }

            stopObfs4.setOnClickListener {
                IPtProxy.stopObfs4Proxy()
            }

            findViewById<Button>(R.id.logPorts).setOnClickListener {
                Log.d("golog", "meek ${IPtProxy.meekPort()}")
                Log.d("golog", "obfs2 ${IPtProxy.obfs2Port()}")
                Log.d("golog", "obfs3 ${IPtProxy.obfs3Port()}")
                Log.d("golog", "obfs4 ${IPtProxy.obfs4Port()}")
                Log.d("golog", "scramblesuit ${IPtProxy.scramblesuitPort()}")
            }
        }
    }
}