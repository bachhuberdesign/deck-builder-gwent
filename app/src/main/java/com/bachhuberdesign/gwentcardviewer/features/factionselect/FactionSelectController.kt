package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.FlipChangeHandler
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.helpers.ClickListenerHelper
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.controller_faction_select.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectController : Controller(), FactionSelectMvpContract {

    companion object {
        @JvmStatic val TAG: String = FactionSelectController::class.java.name
    }

    @Inject
    lateinit var presenter: FactionSelectPresenter

    var recyclerView: RecyclerView? = null
    var adapter: FastItemAdapter<FactionItem>? = null
    var isLeaderClickable: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_faction_select)
        activity?.title = "Select Faction"

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        adapter = FastItemAdapter()
        adapter!!.withItemEvent(object : ClickEventHook<FactionItem>() {
            override fun onBindMany(viewHolder: RecyclerView.ViewHolder): MutableList<View>? {
                if (viewHolder is FactionItem.ViewHolder) {
                    return ClickListenerHelper.toList(viewHolder.leader1, viewHolder.leader2, viewHolder.leader3)
                } else {
                    return super.onBindMany(viewHolder)
                }
            }

            override fun onClick(view: View, i: Int, fastAdapter: FastAdapter<FactionItem>, item: FactionItem) {
                if (isLeaderClickable) {
                    isLeaderClickable = false
                    val index = Integer.valueOf(view.tag as String)
                    beginCardExpandAnimation(view as ImageView, item.leaders!![index])
                }
            }
        })

        val layoutManager = LinearLayoutManager(activity)

        recyclerView = view.recycler_view
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadFactions()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    private fun beginCardExpandAnimation(imageView: ImageView, leader: Card) {
        val flip1 = AnimationUtils.loadAnimation(activity, R.anim.card_flip_1)
        val flip2 = AnimationUtils.loadAnimation(activity, R.anim.card_flip_2)

        flip1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                Glide.with(activity)
                        .load(Uri.parse("file:///android_asset/card_backs_1_new.png"))
                        .fitCenter()
                        .dontAnimate()
                        .into(imageView)
                imageView.animation = flip2
                imageView.startAnimation(flip2)
            }
        })

        flip2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                onLeaderSelected(leader)
            }
        })

        imageView.animation = flip1
        imageView.startAnimation(flip1)
    }

    override fun onFactionsLoaded(factions: List<Faction>) {
        factions.forEach { faction ->
            val item: FactionItem = FactionItem()
            item.factionName = faction.name
            item.factionDescription = faction.effect
            item.backgroundUrl = faction.iconUrl
            item.leaders = faction.leaders
            adapter?.add(item)
        }

        adapter?.notifyDataSetChanged()
    }

    override fun onLeaderSelected(leader: Card) {
        router.pushController(RouterTransaction.with(LeaderConfirmController(leader))
                .pushChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.RIGHT))
                .popChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.LEFT)))

        isLeaderClickable = true
    }

}