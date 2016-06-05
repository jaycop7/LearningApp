package at.campus02.gang_of_four.learningapp.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashSet;
import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.KategorienListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SaveFrageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SuccessListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;

public class RestDataClientTest {
    Context context = null;
    RestDataClient restClient = null;

    public RestDataClientTest(Context context) {
        this.context = context;
        restClient = new RestDataClient();
    }

    public void requestFragen() {
        restClient.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                Log.i("Success", "Fragen success, count " + fragen.size());
            }

            @Override
            public void error() {
                Log.e("Error", "Error requesting Fragen.");
            }
        });
    }

    public void requestKategorien() {
        restClient.getKategorien(new KategorienListener() {
            @Override
            public void success(List<String> kategorien) {
                Log.i("Success", "Kategorien success, count " + kategorien.size());
            }

            @Override
            public void error() {
                Log.e("Error", "Error requesting Kategorien.");
            }
        });
    }

    public void createFrage() {
        Frage frage = getTestFrage();
        restClient.createFrage(frage, new SaveFrageListener() {
            @Override
            public void success(String guid) {
                Log.i("Success", "Testfrage erfolgreich erstellt.");
            }

            @Override
            public void error() {
                Log.e("Error", "Fehler beim Erstellen der Testfrage.");
            }
        });
    }

    public void updateFragen() {
        restClient.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                for (Frage f : fragen) {
                    if ("Frage200".equals(f.getFragetext())) {
                        f.setSchwierigkeitsgrad(2);
                        restClient.updateFrage(f, new SaveFrageListener() {
                            @Override
                            public void success(String guid) {
                                Log.i("Success", "Testfrage erfolgreich upgedated.");
                            }

                            @Override
                            public void error() {
                                Log.e("Error", "Fehler beim Update der Testfrage.");
                            }
                        });
                    }
                }
            }

            @Override
            public void error() {

            }
        });

    }

    public void deleteTestFragen() {
        Preferences.setWiederholungsFragenIds(new HashSet<String>(), context);
        Preferences.setEigeneFragenIds(new HashSet<String>(), context);
        restClient.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                for (Frage f : fragen) {

                    if (f.getFragetext() != null && f.getFragetext().contains("Go4")) {
                        restClient.deleteFrage(f, new SuccessListener() {
                            @Override
                            public void success() {
                                Log.i("Success", "Testfragen erfolgreich gelöscht.");
                            }

                            @Override
                            public void error() {
                                Log.e("Error", "Fehler beim Löschen der Testfragen.");
                            }
                        });
                    }
                }
            }

            @Override
            public void error() {

            }
        });
    }

    public void deleteAlleFragen() {
        Preferences.setWiederholungsFragenIds(new HashSet<String>(), context);
        Preferences.setEigeneFragenIds(new HashSet<String>(), context);
        restClient.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                for (Frage f : fragen) {
                    restClient.deleteFrage(f, new SuccessListener() {
                        @Override
                        public void success() {
                            Log.i("Success", "Testfragen erfolgreich gelöscht.");
                        }

                        @Override
                        public void error() {
                            Log.e("Error", "Fehler beim Löschen der Testfragen.");
                        }
                    });
                }
            }

            @Override
            public void error() {

            }
        });
    }

    public void getImage() {
        restClient.getImage("http://images.clipartpanda.com/test-clip-art-cpa-school-test.png", new ImageListener() {
            @Override
            public void success(Bitmap immage) {
                Log.i("Success", "Image loaded successful.");
            }

            @Override
            public void error() {
                Log.e("Error", "Error loading image.");
            }
        });
    }

    @NonNull
    private Frage getTestFrage() {
        Frage frage = new Frage();
        frage.setFragetext("Frage200");
        frage.setAntwort("Antwort200");
        frage.setBild("http://images.45cat.com/gang-of-four-damaged-goods-fast-product.jpg");
        frage.setKategorie("Software");
        frage.setLaengenUndBreitengrad("47.053748;15.497375");
        frage.setSchwierigkeitsgrad(1);
        return frage;
    }

    public void createTestFragen() {
        Frage frage1 = new Frage();
        frage1.setFragetext("Go4: Welche der folgenden Sportarten ist nicht olympisch - Taekwondo, Beachvolleyball, Kung Fu, Trampolinspringen, Baseball?");
        frage1.setAntwort("Kung Fu");
        frage1.setBild("https://s-media-cache-ak0.pinimg.com/736x/63/25/83/6325838c7f63fd1e3281bcb9e6d29165.jpg");
        frage1.setKategorie("Sport");
        frage1.setSchwierigkeitsgrad(1);
        frage1.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage1, new CreateFragenListenerImpl());

        Frage frage2 = new Frage();
        frage2.setFragetext("Go4: Welches Land wurde am häufigsten Fußballweltmeister?");
        frage2.setAntwort("Brasilien");
        frage2.setKategorie("Sport");
        frage2.setSchwierigkeitsgrad(0);
        frage2.setBild("http://www.spox.com/de/sport/fussball/wm/wm2010/0809/Bilder/ronaldinho-am-boden-514.jpg");
        frage2.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage2, new CreateFragenListenerImpl());

        Frage frage3 = new Frage();
        frage3.setFragetext("Go4: Was war die höchste Geschwindigkeit, die je ein Fallschirmspringer im freien Fall erreicht hat?");
        frage3.setAntwort("1.357,6 km/h");
        frage3.setKategorie("Sport");
        frage3.setSchwierigkeitsgrad(3);
        frage3.setBild("http://globalmetalapocalypse.weebly.com/uploads/7/3/0/7/7307465/368594095.jpg");
        frage3.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage3, new CreateFragenListenerImpl());

        Frage frage4 = new Frage();
        frage4.setFragetext("Go4: Wieviel Prozent der Erdoberfläche sind (noch) von Regenwald bedeckt?");
        frage4.setAntwort("6 %");
        frage4.setKategorie("Dschungel");
        frage4.setSchwierigkeitsgrad(3);
        frage4.setBild("http://wfiles.brothersoft.com/a/amazon-forest_94321-1152x864.jpg");
        frage4.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage4, new CreateFragenListenerImpl());

        Frage frage5 = new Frage();
        frage5.setFragetext("Go4: Wie viele Urwaldbäume werden ungefähr pro Minute umgesägt?");
        frage5.setAntwort("2.000");
        frage5.setKategorie("Dschungel");
        frage5.setSchwierigkeitsgrad(3);
        frage5.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage5, new CreateFragenListenerImpl());

        Frage frage6 = new Frage();
        frage6.setFragetext("Go4: Wie hoch ist der höchste Baum der Welt?");
        frage6.setAntwort("112 m");
        frage6.setKategorie("Dschungel");
        frage6.setSchwierigkeitsgrad(1);
        frage6.setBild("http://img.abendblatt.de/img/stormarn/crop120392578/8382608121-w820-cv16_9-q85/Mexikanische-Sumpfzypresse.jpg");
        frage6.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage6, new CreateFragenListenerImpl());

        Frage frage7 = new Frage();
        frage7.setFragetext("Go4: Was hat James Watt erfunden?");
        frage7.setAntwort("Dampfmaschine");
        frage7.setKategorie("Geschichte");
        frage7.setSchwierigkeitsgrad(2);
        frage7.setBild("https://kinneil.files.wordpress.com/2011/01/jameswatt-fromvictorianbook1.jpg");
        frage7.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage7, new CreateFragenListenerImpl());

        Frage frage8 = new Frage();
        frage8.setFragetext("Go4:  In welchem Gebäude hielten die alten Römer ihre Gladiatorenwettkämpfe ab?");
        frage8.setAntwort("Kolosseum");
        frage8.setBild("http://bilder.t-online.de/b/64/42/93/04/id_64429304/610/tid_da/das-kolosseum-in-rom-.jpg");
        frage8.setKategorie("Geschichte");
        frage8.setSchwierigkeitsgrad(1);
        frage8.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage8, new CreateFragenListenerImpl());

        Frage frage9 = new Frage();
        frage9.setFragetext("Go4:  Welcher König baute den Tempel in Jerusalem?");
        frage9.setAntwort("Salomo");
        frage9.setKategorie("Geschichte");
        frage9.setSchwierigkeitsgrad(1);
        frage9.setBild("http://seedsmission.org/wp/wp-content/uploads/2012/11/templeillus.jpg");
        frage9.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage9, new CreateFragenListenerImpl());

        Frage frage10 = new Frage();
        frage10.setFragetext("Go4:  In welcher Stadt steht der von Gustave Eiffel für die Weltausstellung 1889 erbaute Turm?");
        frage10.setAntwort("Die Stadt liegt in Frankreich und heißt Paris. Sie hat .... Einwohner, blablablablablabl ablablablabl ablablablablablablabl ablablablablabla blablablabla blablabla blablablablablablablab lablablab lablablabl ablablablab lablab l a blablablabl ablablablablab lablablab lablablab lablablablab lablablab lablabla blablabla blabla blablabl ablabl ablablab lablabla");
        frage10.setKategorie("Allgemeinwissen");
        frage10.setSchwierigkeitsgrad(0);
        frage10.setBild("http://media2.giga.de/2015/03/shutterstock_125112029.jpg");
        frage10.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage10, new CreateFragenListenerImpl());

        Frage frage11 = new Frage();
        frage11.setFragetext("Go4:  Was hat der Arme, was der Reiche nicht hat, was der Verschwender spart und der Geizige gibt?");
        frage11.setAntwort("Nichts");
        frage11.setBild("http://cms.groh-design.de/files/nichts_teaserbox3.jpg");
        frage11.setKategorie("Allgemeinwissen");
        frage11.setSchwierigkeitsgrad(0);
        frage11.setLaengenUndBreitengrad("47.411895;15.212652");
        restClient.createFrage(frage11, new CreateFragenListenerImpl());

        Frage frage12 = new Frage();
        frage12.setFragetext("Go4:  Wie lange braucht das Licht in etwa von der Sonne zur Erde?");
        frage12.setAntwort("8 Minuten");
        frage12.setBild("http://www.on-zine.net/wp-content/bilder/licht.jpg");
        frage12.setKategorie("Allgemeinwissen");
        frage12.setSchwierigkeitsgrad(1);
        frage12.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage12, new CreateFragenListenerImpl());

        Frage frage13 = new Frage();
        frage13.setFragetext("Go4:  Wo steht das höchste Haus der Welt?");
        frage13.setAntwort("Dubai");
        frage13.setKategorie("Allgemeinwissen");
        frage13.setSchwierigkeitsgrad(1);
        frage13.setBild("http://imgs.su/tmp/2014-04-25/1398422399-504.jpg");
        frage13.setLaengenUndBreitengrad("47.411895;15.212652");
        restClient.createFrage(frage13, new CreateFragenListenerImpl());

        Frage frage14 = new Frage();
        frage14.setFragetext("Go4:  Wie heißt der höchste Berg Europas?");
        frage14.setAntwort("Montblanc (4.810 m)");
        frage14.setKategorie("Erdkunde");
        frage14.setSchwierigkeitsgrad(1);
        frage14.setBild("http://www.alpaventure.fr/wp-content/uploads/2013/05/vol_mont_blanc.jpg");
        frage14.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage14, new CreateFragenListenerImpl());

        Frage frage15 = new Frage();
        frage15.setFragetext("Go4: Welches Gebirge trennt Europa und Asien?");
        frage15.setAntwort("Ural");
        frage15.setKategorie("Erdkunde");
        frage15.setSchwierigkeitsgrad(2);
        frage15.setBild("http://phzh.educanet2.ch/asien3/.ws_gen/12/Uralgebirge.jpg");
        frage15.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage15, new CreateFragenListenerImpl());

        Frage frage16 = new Frage();
        frage16.setFragetext("Go4: Bor ist ein ...?");
        frage16.setAntwort("Halbleiter");
        frage16.setKategorie("Chemie");
        frage16.setBild("http://www.chemie-master.de/pse/B_DSCN1026.jpg");
        frage16.setSchwierigkeitsgrad(3);
        frage16.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage16, new CreateFragenListenerImpl());

        Frage frage17 = new Frage();
        frage17.setFragetext("Go4: Wie heißt der längste Fluss der Erde?");
        frage17.setAntwort("Der Nil ist der längste Fluss der Erde mit 6.671 km");
        frage17.setKategorie("Erdkunde");
        frage17.setSchwierigkeitsgrad(0);
        frage17.setBild("http://www.nathape.com/images/LB200812Aegypt%20(18).jpg");
        frage17.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage17, new CreateFragenListenerImpl());

        Frage frage18 = new Frage();
        frage18.setFragetext("Go4: Nenne die flächenmäßig 5 größten Länder der Erde!");
        frage18.setAntwort("Russland, Kanada, USA, China, Brasilien");
        frage18.setKategorie("Erdkunde");
        frage18.setSchwierigkeitsgrad(1);
        frage17.setLaengenUndBreitengrad("47.411895;15.212652");
        restClient.createFrage(frage18, new CreateFragenListenerImpl());

        Frage frage19 = new Frage();
        frage19.setFragetext("Go4: Nenne die 3 bevölkerungsreichsten Länder der Erde!");
        frage19.setAntwort("China, Indien, USA");
        frage19.setKategorie("Erdkunde");
        frage19.setSchwierigkeitsgrad(1);
        frage19.setBild("http://noe.orf.at/static/images/site/oeka/20111042/volkszaehlung_body_lia.5012131.jpg");
        frage19.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage19, new CreateFragenListenerImpl());

        Frage frage20 = new Frage();
        frage20.setFragetext("Go4: Wie nennt man Calciumhydroxid noch?");
        frage20.setAntwort("gelöschter Kalk");
        frage20.setBild("http://www.industriekalk.at/assets/images/6/Kalkhydrat_lose-570f4226.jpg");
        frage20.setKategorie("Chemie");
        frage20.setSchwierigkeitsgrad(3);
        frage20.setLaengenUndBreitengrad("47.053748;15.497375");
        restClient.createFrage(frage20, new CreateFragenListenerImpl());

        Frage frage21 = new Frage();
        frage21.setFragetext("Go4: Woraus gewinnt man Aluminium?");
        frage21.setAntwort("Bauxit");
        frage21.setKategorie("Chemie");
        frage21.setSchwierigkeitsgrad(2);
        frage21.setBild("http://www.constellium.com/var/constellium/storage/images/media/images/aluminium-billets/42574-1-eng-GB/aluminium-billets.png");
        frage21.setLaengenUndBreitengrad("47.411895;15.212652");
        restClient.createFrage(frage21, new CreateFragenListenerImpl());
    }

    private class CreateFragenListenerImpl implements SaveFrageListener {
        @Override
        public void success(String guid) {
            Log.i("Success", "Testfrage wurde erstellt");
        }

        @Override
        public void error() {
            Log.e("Error", "Testfrage konnte nicht erstellt werden");
        }
    }
}
