package com.twofa.duosec.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.twofa.duosec.R
import com.twofa.duosec.database.DuosecDatabase
import com.twofa.duosec.databinding.ActivityHomeBinding
import com.twofa.duosec.fingerprint.FingerPrintActivity
import com.twofa.duosec.models.jwt.JwtPayloadDatabase
import com.twofa.duosec.registration.RegistrationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Duo Sec"

        val dao = DuosecDatabase.getInstance(this).jwtPayloadDao

        CoroutineScope(Dispatchers.IO).launch {
            val payloadsList: List<JwtPayloadDatabase> = dao.getAll()
            withContext(Dispatchers.Main) {
                binding.rvCompanyData.layoutManager = LinearLayoutManager(this@HomeActivity)
                binding.rvCompanyData.adapter = JwtPayloadAdapter(payloadsList)
            }
        }

        binding.fabAddCompany.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.itemId
        if (id == R.id.action_item_one) {
            startActivity(Intent(this, FingerPrintActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}