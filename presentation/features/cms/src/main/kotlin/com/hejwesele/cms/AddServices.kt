package com.hejwesele.cms

import com.hejwesele.intent.IntentData
import com.hejwesele.intent.IntentPackage
import com.hejwesele.intent.IntentType
import com.hejwesele.services.ServicesRepository
import com.hejwesele.services.model.Service
import com.hejwesele.services.model.ServiceDetails
import com.hejwesele.services.model.ServiceType
import com.hejwesele.services.model.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class AddServices @Inject constructor(
    private val repository: ServicesRepository
) {

    @Suppress("LongMethod")
    suspend operator fun invoke(services: Services? = null) = withContext(Dispatchers.IO) {
        repository.addServices(
            Services(
                partners = listOf(
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.VENUE,
                        title = "Lokal",
                        name = "Weranda",
                        description = "Przeuroczy, klimatyczny dworek w rustykalnym stylu. Na werandzie zrobisz najlepsze zdjęcia, " +
                            "a chwilę wytchnienia znajdziesz w ogrodzie.",
                        details = listOf(
                            ServiceDetails(
                                title = "Kilka słów o Werandzie",
                                content = listOf(
                                    "Zapraszamy do Werandy, magicznego miejsca, w którym zorganizujemy Wam ślub i wesele, chrzciny," +
                                        " komunie, urodziny i inne uroczystości indywidualnie i niesztampowo, w naturalnej i swobodn" +
                                        "ej atmosferze. Nasz lokal zawdzięcza swój niepowtarzalny wygląd ciężkiej pracy i niepohamow" +
                                        "anej wyobraźni właścicieli. Każde przyjęcie ma swój wyjątkowy klimat i na długo pozostaje w" +
                                        " pamięci uczestników.",
                                    "Powstaniu Werandy przyświecał jeden prosty cel – bliski kontakt z naturą. W naszym lokalu nie s" +
                                        "tawiamy na konwenanse i utarte schematy. Pozwalamy puścić wodze wyobraźni i poczuć klimat o" +
                                        "twartej przestrzeni.",
                                    "Bujna roślinność, klimatyczne, dyskretnie oświetlone alejki naszego ogrodu oraz strefy odpoczyn" +
                                        "ku i wyciszenia stanowią perfekcyjne miejsce do realizacji ślubów plenerowych – wszyscy koc" +
                                        "hamy kontakt z naturą.",
                                    "Każdy Klient traktowany jest indywidualnie, a my jako zespół jesteśmy otwarci i gotowi na spros" +
                                        "tanie najwyższym oczekiwaniom. Zadbamy o każdy detal i dopilnujemy, aby wszystko było na na" +
                                        "jwyższym poziomie. Lubimy wyzwania i nowe projekty, których dotychczas nie mieliśmy okazji " +
                                        "realizować.",
                                    "To wszystko tworzy niepowtarzalne momenty, które złożą się na najpiękniejszy dzień Waszego życi" +
                                        "a. Wizyta w Werandzie na długo pozostanie w pamięci naszych Gości."
                                )
                            )
                        ),
                        image = "",
                        intents = listOf(
                            IntentData(
                                intentType = IntentType.INSTAGRAM,
                                intentPackage = IntentPackage.instagram,
                                intentUrl = "https://www.instagram.com/weranda_przyjecia/"
                            ),
                            IntentData(
                                intentType = IntentType.WWW,
                                intentPackage = null,
                                intentUrl = "http://werandaprzyjecia.pl/"
                            ),
                            IntentData(
                                intentType = IntentType.GOOGLE_MAPS,
                                intentPackage = IntentPackage.google_maps,
                                intentUrl = "https://goo.gl/maps/LqduYenksrZgtdW38"
                            )
                        )
                    ),
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.MUSIC,
                        title = "Muzyka",
                        name = "DJ Domin",
                        description = "Tupanie nóżką do Krawczyka, potem polka z Baciarami, a na koniec Cypis dla wytrwałych, jedno " +
                            "jest pewne - szykuje się zabawa stulecia!",
                        details = listOf(
                            ServiceDetails(
                                title = "Best DJ ever",
                                content = listOf(
                                    "Duet dwóch sympatycznych chłopaków którzy są w branży weselnej od kilku lat i zagrali setki imp" +
                                        "rez w całej Polsce. Potrafimy świetnie komunikować się z klientami oraz gośćmi oraz staramy" +
                                        " się dostosowywać do danego klimatu oraz preferencji. Posiadamy własne profesjonalne nagłoś" +
                                        "nienie i oświetlenie oraz mamy bogaty repertuar z uwzględnieniem playlisty klientów."
                                )
                            )
                        ),
                        image = "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/images%2Fservices%2F-NWHcD" +
                            "epaWFQlvP9yDyn%2Fdj_domin.jpg?alt=media&token=1c39456c-0bf2-4316-91f9-1d8b319b8bfa",
                        intents = listOf(
                            IntentData(
                                intentType = IntentType.INSTAGRAM,
                                intentPackage = IntentPackage.instagram,
                                intentUrl = "https://www.instagram.com/domindj/"
                            ),
                            IntentData(
                                intentType = IntentType.WWW,
                                intentPackage = null,
                                intentUrl = "https://www.facebook.com/domindj/"
                            )
                        )
                    ),
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.PHOTO,
                        title = "Zdjęcia",
                        name = "Just Photo",
                        description = "O wyłapanie tych wyjątkowych chwil i uwięzienie ich na zdjęciach zadba duet świetnych fotogra" +
                            "fów. Pamietajcie - uśmiech do zdjęcia!",
                        details = listOf(
                            ServiceDetails(
                                title = "JUSTYNA I TOMEK",
                                content = listOf(
                                    "To dwa zeestawy profesjonalnych oczu skupionych na Tobie! Jesteśmy parą, która uwielbia spełnia" +
                                        "ć i przekraczać oczekiwania.",
                                    "Bardzo cenimy sobie komunikację, chcemy stworzyć i wypracowywać jak najlepszą relację, która po" +
                                        "zwoli nam na bycie na maxa zaangażowanym i zorganizowanym zespołem.",
                                    "Również cenimy spontaniczność, ponieważ niektóre z najcenniejszych momentów życia są nieplanowa" +
                                        "ne.",
                                    "Tworzymy zdjęcia ślubne, sesje rodzinne, ciążowe, sesje indywidualne. Na co dzień pracujemy na " +
                                        "Lubelszczyźnie, ale chętnie odwiedzimy Was w każdym zakątku Polski czy też Europy!",
                                    "Staramy się, aby nasze zdjęcia były nie tylko ładne, ale i ciekawe… by pobudzały wyobraźnię, wz" +
                                        "ruszały i rozśmieszały :)"
                                )
                            )
                        ),
                        image = "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/images%2Fservices%2F-NWHcD" +
                            "epaWFQlvP9yDyn%2Fjust_photo.png?alt=media&token=5fea6fff-37b9-4430-aead-f78fe012a173",
                        intents = listOf(
                            IntentData(
                                intentType = IntentType.INSTAGRAM,
                                intentPackage = IntentPackage.instagram,
                                intentUrl = "https://www.instagram.com/just_photo_st/"
                            ),
                            IntentData(
                                intentType = IntentType.WWW,
                                intentPackage = null,
                                intentUrl = "https://justphoto.pl/"
                            )
                        )
                    ),
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.MOVIE,
                        title = "Film",
                        name = "Vidamach",
                        description = "Wspaniała profesjonalistka Alicja sprawi, że nasz reportaż slubny dorówna jakością najlepszym" +
                            " produkcjom Netflixa!",
                        details = listOf(
                            ServiceDetails(
                                title = "Artystyczne filmy ślubne",
                                content = listOf(
                                    "Cześć! Jestem Alicja. Moją misją jest tworzenie filmów, które zabiorą Was z powrotem do tych dn" +
                                        "i w sposób tak prawdziwy, jak to tylko możliwe. Bo czym jest wspomnienie, jeśli nie zbiorem" +
                                        " uczuć?",
                                    "To co mnie wyróżnia na tle konkurencji to świetny stosunek jakości do ceny. Na rynek ślubny wch" +
                                        "odzę od niedawna, z tego powodu ceny są niższe, a jakość usług na wysokim poziomie.",
                                    "Poznajmy siebie nawzajem i stwórzmy coś wyjątkowego!"
                                )
                            )
                        ),
                        image = "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/images%2Fservices%2F-NWHcD" +
                            "epaWFQlvP9yDyn%2Fvidamach.png?alt=media&token=74173eb2-1a8b-4da7-aaf3-0ccd85cad328",
                        intents = listOf(
                            IntentData(
                                intentType = IntentType.INSTAGRAM,
                                intentPackage = IntentPackage.instagram,
                                intentUrl = "https://www.instagram.com/theasz/"
                            ),
                            IntentData(
                                intentType = IntentType.WWW,
                                intentPackage = null,
                                intentUrl = "https://www.facebook.com/TheAsz/"
                            )
                        )
                    )
                ),
                attractions = listOf(
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.DRINK,
                        title = "Drink Bar",
                        name = null,
                        description = "Samodzielnie skomponuj swój ulubiony drink! Serdecznie polecamy orzeźwiające Mojito, szalone " +
                            "Cuba Libre, a dla lubiących klasę - Cosmopolitan.",
                        details = listOf(
                            ServiceDetails(
                                title = "Mojito",
                                content = listOf(
                                    "1/2 limonki",
                                    "2 łyżeczki brązowego cukru",
                                    "10 listków mięty",
                                    "50 ml białego rumu",
                                    "25 ml wody gazowanej",
                                    "1. Limonkę dokładnie umyć, pokroić na półksiężyce. Włożyć do wysokiej szklanki, dodać cukier. R" +
                                        "ozgnieść ugniataczem aby wycisnąć sok z limonki.",
                                    "2. Listki mięty ugnieść nieco w dłoniach i dodać do szklanki.",
                                    "3. Do połowy wysokości szklanki dodać kruszony lód.",
                                    "4. Dodać rum i wymieszać. Dopełnić kruszonym lodem oraz chlustem wody gazowanej."
                                )
                            )
                        ),
                        image = "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/images%2Fservices%2F-NWHcD" +
                            "epaWFQlvP9yDyn%2Fdrink_bar.jpg?alt=media&token=7ced514b-ed77-4769-9992-e56f0a509ff9",
                        intents = listOf()
                    ),
                    Service(
                        id = UUID.randomUUID().toString(),
                        type = ServiceType.INSTAX,
                        title = "Instax",
                        name = null,
                        description = "Jesteś mistrzem selfie? Cudownie! Nie jesteś? Nie szkodzi, z Intaxem wszystki zdjęcia wygląda" +
                            "ją fantastycznie. Zrób fotę i umieść ją w naszej księdze gości.",
                        details = listOf(
                            ServiceDetails(
                                title = "I Ty zostań dziś fotografem",
                                content = listOf(
                                    "Cieszcie się chwilą i uwiecznijcie ją natychmiastowo dzięki aparatom Instax na Waszym weselu! P" +
                                        "rzyjęcie weselne to wyjątkowa okazja, pełna radości i pięknych momentów, które chcielibyści" +
                                        "e zachować na zawsze. Dlatego proponujemy Wam aparaty Instax - niezwykłe narzędzia, które p" +
                                        "rzemienią Wasze wspomnienia w wyjątkowe, fizyczne odbitki. Pozwólcie gościom na spontaniczn" +
                                        "e i zabawne zdjęcia, które będą doskonałą pamiątką z Waszego wesela. Dzięki aparatom Instax" +
                                        " stworzycie unikalną atmosferę i dodacie mnóstwo uśmiechu do tego wyjątkowego dnia. Wybierz" +
                                        "cie Instax i niech Wasze wspomnienia staną się trwałymi i magazynowanymi wspaniałymi chwila" +
                                        "mi."
                                )
                            )
                        ),
                        image = "https://firebasestorage.googleapis.com/v0/b/hejwesele-test.appspot.com/o/images%2Fservices%2F-NWHcD" +
                            "epaWFQlvP9yDyn%2Finstax.png?alt=media&token=25065e67-c4e6-40c0-93e8-2e7e33109d07",
                        intents = listOf()
                    )
                )
            )
        )
    }
}
