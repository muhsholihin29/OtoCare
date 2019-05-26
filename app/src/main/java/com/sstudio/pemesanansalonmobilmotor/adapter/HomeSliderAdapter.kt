package com.sstudio.pemesanansalonmobilmotor.adapter

import com.sstudio.pemesanansalonmobilmotor.Model.Banner
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class HomeSliderAdapter: SliderAdapter() {

    private val bannerList: List<Banner>? = null

    override fun getItemCount(): Int {
        return bannerList!!.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList!!.get(position).image)
    }
}