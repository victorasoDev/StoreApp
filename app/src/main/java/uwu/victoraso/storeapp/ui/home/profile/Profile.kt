package uwu.victoraso.storeapp.ui.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.ui.components.StoreAppButton
import uwu.victoraso.storeapp.ui.components.StoreAppCard
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.profile.personalinfo.PersonalInfoDialog
import uwu.victoraso.storeapp.ui.home.profile.settings.SettingsDialog
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Profile(
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val profileUiState: ProfileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    when (profileUiState) {
        is ProfileUiState.Success -> {
            Profile(
                userProfile = (profileUiState as ProfileUiState.Success).userProfile,
                restartApp = restartApp,
                onNavigateTo = onNavigateTo,
                onSignOutClick = viewModel::onSignOutClick,
                modifier = modifier
            )
        }
        ProfileUiState.Loading -> { }
        ProfileUiState.Error -> { }
    }
}


@Composable
private fun Profile(
    userProfile: UserProfile,
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onSignOutClick: ((String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            ProfileContent(
                userProfile = userProfile,
                restartApp = restartApp,
                onNavigateTo = onNavigateTo,
                onSignOutClick = onSignOutClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = stringResource(id = R.string.user_profile_title, userProfile.name),
                destinationBarButtonVisible = false,
                onDestinationBarButtonClick = { }
            )
        }
    }
}

@Composable
fun ProfileContent(
    userProfile: UserProfile,
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onSignOutClick: ((String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPersonalInfoDialogShowing by remember { mutableStateOf(false) }
    PersonalInfoDialog(show = isPersonalInfoDialogShowing, onDismiss = { isPersonalInfoDialogShowing = !isPersonalInfoDialogShowing })
    var isSettingsDialogShowing by remember { mutableStateOf(false) }
    SettingsDialog(show = isSettingsDialogShowing, onDismiss = { isSettingsDialogShowing = !isSettingsDialogShowing })

    Column(modifier) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
            )
        )
        StoreAppCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .weight(1f),
        ) {
            Column(
                modifier = Modifier.background(Brush.horizontalGradient(colors = StoreAppTheme.colors.gradientLavander))
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.home_profile),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                    color = StoreAppTheme.colors.textSecondary,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                )
                StoreAppCard(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = StoreAppTheme.colors.uiBackground,
                ) {
                    Column {
                        UserProfile(userProfile, modifier)
                        ProfileOptions(
                            onNavigateTo = onNavigateTo,
                            changePIDialogVisibility = { isPersonalInfoDialogShowing = !isPersonalInfoDialogShowing },
                            changeSettingsDialogVisibility = { isSettingsDialogShowing = !isSettingsDialogShowing }
                        )
                        LogoutButton(
                            restartApp = restartApp,
                            onSignOutClick = onSignOutClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfile(
    userProfile: UserProfile,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        val (divider, name, email) = createRefs()
        createVerticalChain(name, email, chainStyle = ChainStyle.Packed)
        Text(
            text = userProfile.name,
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = parent.start,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Text(
            text = userProfile.email,
            style = MaterialTheme.typography.body1,
            color = StoreAppTheme.colors.textHelp,
            modifier = Modifier.padding(bottom = 20.dp).constrainAs(email) {
                linkTo(
                    start = parent.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    end = parent.end,
                    bias = 0f
                )
            }
        )
        StoreAppDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
private fun ProfileOptions(
    onNavigateTo: (String) -> Unit,
    changePIDialogVisibility: () -> Unit,
    changeSettingsDialogVisibility: () -> Unit,
    profileDestinations: List<ProfileDestination> = profileDestinationsList
) {
    LazyColumn {
        items(profileDestinations) { profileDestination ->
            DesinationRow(
                text = profileDestination.destinationName,
                route = profileDestination.route,
                onNavigateTo = onNavigateTo,
                changePIDialogVisibility = { changePIDialogVisibility() },
                changeSettingsDialogVisibility = { changeSettingsDialogVisibility() }
            )
        }
    }
}

@Composable
private fun DesinationRow(
    text: String,
    route: String,
    onNavigateTo: (String) -> Unit,
    changePIDialogVisibility: () -> Unit,
    changeSettingsDialogVisibility: () -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            when (text) {
                "Personal Info" -> {
                    changePIDialogVisibility()
                }
                "Settings" -> {
                    changeSettingsDialogVisibility()
                }
                else -> {
                    onNavigateTo(route)
                }
            }
        }
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6,
            color = StoreAppTheme.colors.textSecondary,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .wrapContentWidth(Alignment.Start)
                .weight(1f)
        )
    }
    StoreAppDivider(modifier = Modifier.padding(start = 20.dp))
}

@Composable
private fun LogoutButton(
    restartApp: (String) -> Unit,
    onSignOutClick: ((String) -> Unit) -> Unit,
) {
    Spacer(modifier = Modifier.height(35.dp))
    StoreAppButton(
        onClick = { onSignOutClick(restartApp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Logout,
            contentDescription = stringResource(R.string.label_add),
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = stringResource(id = R.string.log_out),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 16.dp)
                .weight(1f),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    StoreAppTheme {
//        Profile(ProfileUiState)
    }
}