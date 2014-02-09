package lecoselol.clumsyninja;

import android.content.Context;
import android.util.Log;
import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasException;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.SaveMode;

import java.util.Collection;
import java.util.List;

public class NoteBaasboxService {
    private static final String COLLECTION = "note";

    private static void insertAll(Collection<Note> notes) {
        Log.e("NoteBaasboxService", "insertAll");
        for (Note note : notes) {
            BaasDocument doc = new BaasDocument(COLLECTION)
                .putString("title", note.getTitle())
                .putString("body", note.getBody())
                .putString("idNostro", note.getId() + "");

            doc.save(SaveMode.IGNORE_VERSION, new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> baasDocumentBaasResult) {
                    Log.e("NoteBaasboxService", "inserito " + baasDocumentBaasResult);
                }
            });
        }
    }

    public static void update(Context ctx, final Collection<Note> notes) {
        BaasBox.Config config = new BaasBox.Config();
        config.apiDomain = "192.168.7.31";
        config.httpPort = 9000;

        BaasBox box = BaasBox.initDefault(ctx, config);

        BaasUser user = BaasUser.withUserName("admin");
        user.setPassword("admin");
        user.login(new BaasHandler<BaasUser>() {
            @Override
            public void handle(BaasResult<BaasUser> baasUserBaasResult) {
                BaasDocument.fetchAll(COLLECTION, new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> result) {
                        try {
                            List<BaasDocument> notesBaas = result.get();
                            for (BaasDocument notesBaa : notesBaas) {
                                BaasDocument.delete(COLLECTION, notesBaa.getId(), null);
                            }
                        }
                        catch (BaasException e) {
                            e.printStackTrace();
                        }
                        finally {
                            insertAll(notes);
                        }
                    }
                });
            }
        });
    }
}
