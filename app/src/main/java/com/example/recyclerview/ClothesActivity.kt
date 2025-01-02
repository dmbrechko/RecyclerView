package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityClothesBinding

class ClothesActivity : AppCompatActivity() {
    private val clothes: MutableList<Cloth> = MutableList(20) { index ->
        Cloth(
            image = R.drawable.cloth,
            name = "Cloth Item #${index + 1}",
            desc = "Description for Cloth Item #${index + 1}"
        )
    }
    private lateinit var clothAdapter: ClothAdapter
    val getUpdate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val cloth = data.getParcelableCompat<Cloth>(DetailsActivity.KEY_CLOTH) ?: return@registerForActivityResult
            val position = data.getIntExtra(DetailsActivity.KEY_POSITION, 0)
            clothes[position] = cloth
            clothAdapter.notifyItemChanged(position)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityClothesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            setSupportActionBar(toolbar)
            listRV.layoutManager = LinearLayoutManager(this@ClothesActivity)
            clothAdapter = ClothAdapter(clothes) { position ->
                val intent = Intent(this@ClothesActivity, DetailsActivity::class.java).apply {
                    putExtra(DetailsActivity.KEY_CLOTH, clothes[position])
                    putExtra(DetailsActivity.KEY_POSITION, position)
                }
                getUpdate.launch(intent)
            }
            listRV.adapter = clothAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_exit -> {
                moveTaskToBack(true)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

data class Cloth(val image: Int, val name: String, val desc: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(name)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cloth> {
        override fun createFromParcel(parcel: Parcel): Cloth {
            return Cloth(parcel)
        }

        override fun newArray(size: Int): Array<Cloth?> {
            return arrayOfNulls(size)
        }
    }
}