package mustafaozhan.github.com.androcat.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_settings.adView
import kotlinx.android.synthetic.main.fragment_settings.inversionSwitch
import kotlinx.android.synthetic.main.fragment_settings.layoutFeedback
import kotlinx.android.synthetic.main.fragment_settings.layoutInversion
import kotlinx.android.synthetic.main.fragment_settings.layoutOnGitHub
import kotlinx.android.synthetic.main.fragment_settings.layoutReportIssue
import kotlinx.android.synthetic.main.fragment_settings.layoutSupport
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.loadAd
import mustafaozhan.github.com.androcat.main.fragment.MainFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun setListeners() {
        inversionSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateInvertSettings(isChecked)
        }
        layoutInversion.setOnClickListener {
            inversionSwitch.isChecked = !inversionSwitch.isChecked
        }
        layoutSupport.setOnClickListener {
            showDialog(
                getString(R.string.support_us),
                getString(R.string.rate_and_support),
                getString(R.string.rate)
            ) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_androcat))))
            }
        }
        layoutFeedback.setOnClickListener { sendFeedBack() }
        layoutOnGitHub.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_project_repository)), false)
        }
        layoutReportIssue.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_report_issue)), false)
        }
    }

    private fun init() {
        inversionSwitch.isChecked = viewModel.getSettings().isInvert
    }

    private fun sendFeedBack() {
        try {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "text/email"
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for AndroCat")
            email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
            startActivity(Intent.createChooser(email, "Send Feedback:"))
        } catch (activityNotFoundException: ActivityNotFoundException) {
            Crashlytics.logException(activityNotFoundException)
            snacky("You do not have any mail application.")
        }
    }

    override fun onResume() {
        adView.loadAd(R.string.banner_ad_id)
        super.onResume()
    }
}