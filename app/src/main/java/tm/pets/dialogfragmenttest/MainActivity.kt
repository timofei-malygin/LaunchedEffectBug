package tm.pets.dialogfragmenttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tm.pets.dialogfragmenttest.sample.SampleFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, SampleFragment::class.java, null)
                .commit()
        }
    }
}