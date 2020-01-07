# Intent Sharing

This project is a brief example of how to share data between apps using [Intents](https://developer.android.com/guide/components/intents-filters) even when apps are not running in the foreground.

## Background

This project exists because I was interested in exploring requesting data from another app without the use of the AccountManager or Content Providers (which are cumbersome), or Shared Preferences (which is now superseded by the FileProvider). The other constraint was the fact that the responsing application should not need an Activity to show to the user. So the preference was to use Services and/or BroadcastReceivers.

With the latest Android O changes, the use of BroadcastReceivers is no longer a viable option when the responding app is not in the foreground. Instead we can use Services that can be launched briefly which can then use a normal broadcast back to the requesting app (which is in the foreground) and keep the responding app in the background. The latest versions of Android require a notification to be shown using the `startForeground()` to inform the user that we've launched the service, but we can quickly dismiss it again once we've received the data.

## Usage

Open both the `shared-token-spike-app-1` and `shared-token-spike-app-2` projects in Android Studio and launch both apps in an emulator. Either app should be able to request data from each other.

![Screenshot](https://thumbs.gfycat.com/BetterDarkFinch-mobile.mp4)

## Risks

This approach does not give the user much control over what is being shared. I think a better approach would be to launch an Activity in the responding app where the user can accept or reject the request for data. This also removes the need for Services and BroadcastReceivers as the launched Activity can return the data via the `setResult()` and `onActivityResult()` methods, which is a safer approach - [link](https://developer.android.com/reference/android/app/Activity#starting-activities-and-getting-results).

![Screenshot](https://thumbs.gfycat.com/UnequaledAgileElectriceel-mobile.mp4)

## License

If this is an open-source project, specify and/or link to a license here. There should also be a `LICENSE.md` file at the root of the repository, which GitHub is able to recognise. Current A2I2 open-source projects use the [BSD 3-Clause](https://github.com/a2i2/surround/blob/master/LICENSE.md) license.

## Contributing

Contributions are most welcome. Before submitting an issue or pull request, please familiarise yourself with the [Contribution Guidelines](./CONTRIBUTING.md).
