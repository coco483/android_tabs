package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.viewactivitypractice.R

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryTab.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryTab : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.gallery_tab_fragment, container, false)
    }
}