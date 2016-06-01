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
        service = new RestDataService(context);
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
                    if ("Frage200".equals(f.getFragetext())) {
                        service.deleteFrage(f, new SuccessListener() {
                            @Override
                            public void success() {
                                Log.i("Success", "Testfrage erfolgreich gelöscht.");
                            }

                            @Override
                            public void error() {
                                Log.e("Error", "Fehler beim Löschen der Testfrage.");
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
}
