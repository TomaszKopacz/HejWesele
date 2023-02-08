package com.hejwesele.home.usecase

import com.hejwesele.invitations.InvitationsRepository
import com.hejwesele.invitations.model.IntentPackage
import com.hejwesele.invitations.model.IntentType
import com.hejwesele.invitations.model.IntentUrl
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.invitations.model.InvitationTile
import com.hejwesele.invitations.model.InvitationTileType
import javax.inject.Inject

class AddInvitation @Inject constructor(
    private val repository: InvitationsRepository
) {

    suspend operator fun invoke(invitation: Invitation) = repository.addInvitation(
        Invitation(
            id = "",
            tiles = listOf(
                InvitationTile(
                    type = InvitationTileType.COUPLE,
                    title = "Tomek Kopacz & Patrycja Sobuś",
                    description = "To my! Dziś bierzemy ślub! Cieszymy się, że jesteście razem z nami i życzymy Wam wyśmienitej " +
                        "zabawy do białego rana!",
                    avatars = listOf(
                        "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/image_tomek.jpg?alt=media&token=" +
                            "158d4ecb-df1b-490a-8fec-b7896d54ad5d",
                        "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/image_patrycja.jpg?alt=media&toke" +
                            "n=b8c18b70-0e86-450c-b785-97c18ddf28a7"
                    ),
                    intents = listOf(
                        IntentUrl(
                            type = IntentType.INSTAGRAM,
                            intentPackage = IntentPackage.instagram,
                            url = "https://www.instagram.com/tomek_kopacz/"
                        ),
                        IntentUrl(
                            type = IntentType.INSTAGRAM,
                            intentPackage = IntentPackage.instagram,
                            url = "https://www.instagram.com/pati.sobus/"
                        )
                    )
                ),
                InvitationTile(
                    type = InvitationTileType.DATE,
                    title = "14.07.2024",
                    subtitle = "15:30",
                    description = "Lato, wakacje, lipiec, niedziela - idealna pora na zabawę do białego rana!",
                    avatars = emptyList(),
                    intents = emptyList()
                ),
                InvitationTile(
                    type = InvitationTileType.CHURCH,
                    title = "Parafia WNMP w Przecławiu",
                    description = "Ceremonia ślubna odbędzie się w uroczym kościele w Przecławiu. Kliknij, aby dowiedzieć się jak" +
                        " tam dojechać.",
                    avatars = listOf(
                        "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/image_church.png?alt=media&tok" +
                            "en=5c07f09c-76e4-4d3f-8178-f41674b97c36"
                    ),
                    intents = listOf(
                        IntentUrl(
                            type = IntentType.GOOGLE_MAPS,
                            intentPackage = IntentPackage.google_maps,
                            url = "https://goo.gl/maps/xZHrPDVBJXXTQKMr6"
                        )
                    )
                ),
                InvitationTile(
                    type = InvitationTileType.VENUE,
                    title = "Weranda w Komorowie",
                    description = "Na zabawę weselną udajemy się do Komorowa. Czeka tu na Was piękny rustykalny lokal z werandą," +
                        " ogrodem i  girlandami!",
                    avatars = listOf(
                        "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/image_venue.png?alt=media&tok" +
                            "en=b312eae3-387f-4fd3-8985-531b98a71ee4"
                    ),
                    intents = listOf(
                        IntentUrl(
                            type = IntentType.INSTAGRAM,
                            intentPackage = IntentPackage.instagram,
                            url = "https://www.instagram.com/weranda_przyjecia/"
                        ),
                        IntentUrl(
                            type = IntentType.WWW,
                            intentPackage = null,
                            url = "http://werandaprzyjecia.pl/"
                        ),
                        IntentUrl(
                            type = IntentType.GOOGLE_MAPS,
                            intentPackage = IntentPackage.google_maps,
                            url = "https://goo.gl/maps/LqduYenksrZgtdW38"
                        )
                    )
                ),
                InvitationTile(
                    type = InvitationTileType.WISHES,
                    title = "Parę słów od nas",
                    description = "Kochani! Niezmiernie się cieszymy, że jesteście razem z nami w najpiękniejszym dla Nas dniu. Tańc" +
                        "zcie, śpiewajcie i opróżniajcie kieliszki!",
                    avatars = emptyList(),
                    intents = emptyList()
                )
            ),
            )
        )
}
