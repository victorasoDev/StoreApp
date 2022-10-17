package uwu.victoraso.storeapp.ui.home.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
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
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.profile.personalinfo.PersonalInfoDialog
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.mirroringIcon

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Profile(
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Log.d(DEBUG_TAG, "antes de profileUiState")
    val profileUiState: ProfileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    when (profileUiState) {
        is ProfileUiState.Success -> {
            Profile(
                profileUiState = profileUiState,
                restartApp = restartApp,
                onNavigateTo = onNavigateTo,
                onSignOutClick = viewModel::onSignOutClick,
                modifier = modifier
            )
        }
        ProfileUiState.Loading -> {
            //TODO()
        }
        ProfileUiState.Error -> {
            //TODO()
        }
    }
}


@Composable
private fun Profile(
    profileUiState: ProfileUiState,
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onSignOutClick: ((String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO cargar los datos del usuario
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            ProfileContent(
                restartApp = restartApp,
                onNavigateTo = onNavigateTo,
                onSignOutClick = onSignOutClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = "Hi Victoraso!",
                onDestinationBarButtonClick = { }
            )
        }
    }
}

@Composable
fun ProfileContent(
    restartApp: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onSignOutClick: ((String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer( //TODO sustituir todos los del proyecto pasándolo a Utils
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
                modifier = Modifier.background(Brush.horizontalGradient(colors = StoreAppTheme.colors.gradient2_2))
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Profile",
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
                        //TODO probar constraint layout sería foto, derecha nombre y debajo email
                        UserProfile(modifier)
                        ProfileOptions(onNavigateTo)
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
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 20.dp)
    ) {
        val (divider, image, name, email) = createRefs()
        createVerticalChain(name, email, chainStyle = ChainStyle.Packed)
        ProductImage(
            imageUrl = "",
            elevation = 4.dp,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 20.dp)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = "Victoraso",
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Text(
            text = "victorsgmii@gmail.com",
            style = MaterialTheme.typography.body1,
            color = StoreAppTheme.colors.textHelp,
            modifier = Modifier.constrainAs(email) {
                linkTo(
                    start = image.end,
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
    profileDestinations: List<ProfileDestination> = profileDestinationsList
) {
    var isDialogShowing by remember { mutableStateOf(false) }
    PersonalInfoDialog(show = isDialogShowing, onDismiss = { isDialogShowing = !isDialogShowing })

    LazyColumn {
        items(profileDestinations) { profileDestination ->
            DesinationRow(
                text = profileDestination.destinationName,
                route = profileDestination.route,
                onNavigateTo = onNavigateTo,
                changeDialogVisibility = { isDialogShowing = !isDialogShowing }
            )
        }
    }
}

@Composable
private fun DesinationRow(
    text: String,
    route: String,
    onNavigateTo: (String) -> Unit,
    changeDialogVisibility: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { //TODO: buscar otra manera
            if (text != "Personal Info") {
                onNavigateTo(route)
            } else {
                changeDialogVisibility()
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
        IconButton(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = mirroringIcon(
                    ltrIcon = Icons.Outlined.ArrowForward,
                    rtlIcon = Icons.Outlined.ArrowBack
                ),
                tint = StoreAppTheme.colors.brand,
                contentDescription = null
            )
        }
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