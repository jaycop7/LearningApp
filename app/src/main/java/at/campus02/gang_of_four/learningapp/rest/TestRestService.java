package at.campus02.gang_of_four.learningapp.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.KategorienListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SuccessListener;

public class TestRestService {
    Context context = null;
    RestDataService service = null;

    public TestRestService(Context context) {
        this.context = context;
        service = new RestDataService();
    }

    public void requestFragen() {
        service.getAlleFragen(new FragenListener() {
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
        service.getKategorien(new KategorienListener() {
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
        service.createFrage(frage, new SuccessListener() {
            @Override
            public void success() {
                Log.i("Success", "Testfrage erfolgreich erstellt.");
            }

            @Override
            public void error() {
                Log.e("Error", "Fehler beim Erstellen der Testfrage.");
            }
        });
    }

    public void updateFragen() {
        service.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                for (Frage f : fragen) {
                    if ("Frage200".equals(f.getFragetext())) {
                        f.setSchwierigkeitsgrad(2);
                        service.updateFrage(f, new SuccessListener() {
                            @Override
                            public void success() {
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
        service.getAlleFragen(new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                for (Frage f : fragen) {
                    if (f.getFragetext().contains("Go4")) {
                        service.deleteFrage(f, new SuccessListener() {
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

    public void getImage() {
        service.getImage("http://images.clipartpanda.com/test-clip-art-cpa-school-test.png", new ImageListener() {
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
        frage.setSchwierigkeitsgrad(1);
        return frage;
    }

    public void createTestFragen() {
        Frage frage1 = new Frage();
        frage1.setFragetext("Go4: Welche Sportart ist nicht olympisch?");
        frage1.setAntwort("Kung Fu");
        frage1.setKategorie("Sport");
        frage1.setSchwierigkeitsgrad(1);
        frage1.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage1, createFragenListener());

        Frage frage2 = new Frage();
        frage2.setFragetext("Go4: Welches Land wurde am häufigsten Fußballweltmeister?");
        frage2.setAntwort("Brasilien");
        frage2.setKategorie("Sport");
        frage2.setSchwierigkeitsgrad(0);
        frage2.setBild("http://wallpaperstock.net/brasilien-flagge-wallpapers_32951_1920x1200.jpg");
        frage2.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage2, createFragenListener());

        Frage frage3 = new Frage();
        frage3.setFragetext("Go4: Was war die höchste Geschwindigkeit, die je ein Fallschirmspringer im freien Fall erreicht hat?");
        frage3.setAntwort("1.357,6 km/h");
        frage3.setKategorie("Sport");
        frage3.setSchwierigkeitsgrad(3);
        frage3.setBild("http://globalmetalapocalypse.weebly.com/uploads/7/3/0/7/7307465/368594095.jpg");
        frage3.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage3, createFragenListener());

        Frage frage4 = new Frage();
        frage4.setFragetext("Go4: Wieviel Prozent der Erdoberfläche sind (noch) von Regenwald bedeckt?");
        frage4.setAntwort("6 %");
        frage4.setKategorie("Dschungel");
        frage4.setSchwierigkeitsgrad(3);
        frage4.setBild("http://wfiles.brothersoft.com/a/amazon-forest_94321-1152x864.jpg");
        frage4.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage4, createFragenListener());

        Frage frage5 = new Frage();
        frage5.setFragetext("Go4: Wie viele Urwaldbäume werden ungefähr pro Minute umgesägt?");
        frage5.setAntwort("2.000");
        frage5.setKategorie("Dschungel");
        frage5.setSchwierigkeitsgrad(3);
        frage5.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage5, createFragenListener());

        Frage frage6 = new Frage();
        frage6.setFragetext("Go4: Wie hoch ist der höchste Baum?");
        frage6.setAntwort("112 m");
        frage6.setKategorie("Dschungel");
        frage6.setSchwierigkeitsgrad(1);
        frage6.setBild("http://static.cosmiq.de/data/de/087/6b/0876b8c97be8439f9b2dd6e6219c67cd_1_orig.jpg");
        frage6.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage6, createFragenListener());

        Frage frage7 = new Frage();
        frage7.setFragetext("Go4: Was hat James Watt erfunden?");
        frage7.setAntwort("Dampfmaschine");
        frage7.setKategorie("Geschichte");
        frage7.setSchwierigkeitsgrad(2);
        frage7.setBild("https://de.wikipedia.org/wiki/James_Watt#/media/File:James_Watt_by_Henry_Howard.jpg");
        frage7.setLaengenUndBreitengrad("47.0848496;15.443150100000025");
        service.createFrage(frage7, createFragenListener());

        Frage frage8 = new Frage();
        frage8.setFragetext("Go4:  In welchen Gebäude hielten die alten Römer ihre Gladiatorenwettkämpfe ab?");
        frage8.setAntwort("Kolosseum");
        frage8.setKategorie("Geschichte");
        frage8.setSchwierigkeitsgrad(1);
        frage8.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage8, createFragenListener());

        Frage frage9 = new Frage();
        frage9.setFragetext("Go4:  Welcher König baute den Tempel in Jerusalem?");
        frage9.setAntwort("Salomo");
        frage9.setKategorie("Geschichte");
        frage9.setSchwierigkeitsgrad(1);
        frage9.setBild("http://seedsmission.org/wp/wp-content/uploads/2012/11/templeillus.jpg");
        frage9.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage9, createFragenListener());

        Frage frage10 = new Frage();
        frage10.setFragetext("Go4:  In welcher Stadt steht der von Gustave Eiffel für die Weltausstellung 1889 erbaute Turm?");
        frage10.setAntwort("Die Stadt liegt in Frankreich und heißt Paris. Sie hat .... Einwohner, blablablablablabl ablablablabl ablablablablablablabl ablablablablabla blablablabla blablabla blablablablablablablab lablablab lablablabl ablablablab lablab l a blablablabl ablablablablab lablablab lablablab lablablablab lablablab lablabla blablabla blabla blablabl ablabl ablablab lablabla");
        frage10.setKategorie("Allgemeinwissen");
        frage10.setSchwierigkeitsgrad(0);
        frage10.setBild("http://www.historylines.net/img/eiffel/eiffel6.jpg");
        frage10.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage10, createFragenListener());

        Frage frage11 = new Frage();
        frage11.setFragetext("Go4:  Was hat der Arme, was der Reiche nicht hat, was der Verschwender spart und der Geizige gibt?");
        frage11.setAntwort("Nichts");
        frage11.setKategorie("Allgemeinwissen");
        frage11.setSchwierigkeitsgrad(0);
        frage11.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage11, createFragenListener());

        Frage frage12 = new Frage();
        frage12.setFragetext("Go4:  Wie lange braucht das Licht in etwa von der Sonne zur Erde?");
        frage12.setAntwort("8 Minuten");
        frage12.setKategorie("Allgemeinwissen");
        frage12.setSchwierigkeitsgrad(1);
        frage12.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage12, createFragenListener());

        Frage frage13 = new Frage();
        frage13.setFragetext("Go4:  Wo steht das höchste Haus der Welt?");
        frage13.setAntwort("Dubai");
        frage13.setKategorie("Allgemeinwissen");
        frage13.setSchwierigkeitsgrad(1);
        frage13.setBild("http://www.omicrono.com/wp-content/uploads/2016/05/objeto-caro-4.jpg");
        frage13.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage13, createFragenListener());

        Frage frage14 = new Frage();
        frage14.setFragetext("Go4:  Wie heißt der höchste Berg Europas?");
        frage14.setAntwort("Montblanc (4.810 m)");
        frage14.setKategorie("Erdkunde");
        frage14.setSchwierigkeitsgrad(1);
        frage14.setBild("http://www.alpaventure.fr/wp-content/uploads/2013/05/vol_mont_blanc.jpg");
        frage14.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage14, createFragenListener());

        Frage frage15 = new Frage();
        frage15.setFragetext("Go4: Welches Gebirge trennt Europa und Asien?");
        frage15.setAntwort("Ural");
        frage15.setKategorie("Erdkunde");
        frage15.setSchwierigkeitsgrad(2);
        frage15.setBild("http://phzh.educanet2.ch/asien3/.ws_gen/12/Uralgebirge.jpg");
        frage15.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage15, createFragenListener());

        Frage frage16 = new Frage();
        frage16.setFragetext("Go4: Welches Gebirge trennt Europa und Asien?");
        frage16.setAntwort("Ural");
        frage16.setKategorie("Erdkunde");
        frage16.setSchwierigkeitsgrad(1);
        frage16.setBild("http://phzh.educanet2.ch/asien3/.ws_gen/12/Uralgebirge.jpg");
        frage16.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage16, createFragenListener());

        Frage frage17 = new Frage();
        frage17.setFragetext("Go4: Wie heißt der längste Fluss der Erde?");
        frage17.setAntwort("Der Nil ist der längste Fluss der Erde mit 6671km");
        frage17.setKategorie("Erdkunde");
        frage17.setSchwierigkeitsgrad(0);
        frage17.setBild("http://www.splinterhh.de/nil/nil2.gif");
        frage17.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage17, createFragenListener());

        Frage frage18 = new Frage();
        frage18.setFragetext("Go4: Nenne die flächenmäßig 5 größten Länder der Erde!");
        frage18.setAntwort("Russland, Kanada, USA, China, Brasilien");
        frage18.setKategorie("Erdkunde");
        frage18.setSchwierigkeitsgrad(1);
        frage18.setBild("http://www.splinterhh.de/nil/nil2.gif");
        service.createFrage(frage18, createFragenListener());

        Frage frage19 = new Frage();
        frage19.setFragetext("Go4: Nenne die flächenmäßig 5 größten Länder der Erde!");
        frage19.setAntwort("Russland, Kanada, USA, China, Brasilien");
        frage19.setKategorie("Erdkunde");
        frage19.setSchwierigkeitsgrad(1);
        frage19.setBild("http://www.splinterhh.de/nil/nil2.gif");
        frage19.setLaengenUndBreitengrad("46.82820758544973;12.765481621026992");
        service.createFrage(frage19, createFragenListener());

    }

    @NonNull
    private SuccessListener createFragenListener() {
        return new SuccessListener() {
            @Override
            public void success() {

            }

            @Override
            public void error() {

            }
        };
    }

}
