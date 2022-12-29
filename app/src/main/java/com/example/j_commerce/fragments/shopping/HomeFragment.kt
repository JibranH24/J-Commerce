package com.example.j_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.j_commerce.R
import com.example.j_commerce.adapters.HomeViewPagerAdapter
import com.example.j_commerce.databinding.FragmentHomeBinding
import com.example.j_commerce.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home){
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            WardrobeFragment(),
            TableFragment(),
            LightsFragment(),
            BedFragment()

        )

        binding.viewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chairs"
                2 -> tab.text = "Wardrobes"
                3 -> tab.text = "Tables"
                4 -> tab.text = "Lights"
                5 -> tab.text = "Beds"
            }
        }.attach()
    }
}