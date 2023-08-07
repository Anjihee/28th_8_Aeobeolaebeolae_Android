package com.surround2023.surround2023.community

import com.surround2023.surround2023.community.Communityadapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivityCommunityholderBinding

class CommunityholderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityholderBinding
    private lateinit var adapter: Communityadapter
    private var userList = mutableListOf<Communitymemo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_communityholder)

        for (i in 1 until 11) {
            val mUser = Communitymemo(i, "user$i")
            userList.add(mUser)
        }
    }

    override fun onResume() {
        super.onResume()

        adapter = Communityadapter(this, userList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}