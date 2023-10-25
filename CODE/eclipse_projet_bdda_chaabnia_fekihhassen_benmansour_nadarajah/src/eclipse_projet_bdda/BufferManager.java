package eclipse_projet_bdda;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferManager {
    private static BufferManager instance = new BufferManager();

    // informations associées à chaque frame
    private ByteBuffer[] buffers;
    private PageId[] pageIds;
    private int[] pinCounts;
    private boolean[] flagDirty;
    private int[] accessCpt;

    public static BufferManager getInstance() {
        return instance;
    }

    public ByteBuffer GetPage(PageId pageId) {
        // on recherche si la page est déjà dans le buffer
        for ( int i = 0 ; i < DBParams.frameCount; i++ ) {
            if ( pageIds[i] != null && pageIds[i].equals(pageId) ) {
                // si page trouvée dans le buffer, mettre à jour le compteur d'accès
                accessCpt[i]++;
                return buffers[i]; // on retourne le buffer correspondant
            }
        }

        // si la page n'est pas dans le buffer, trouvez une frame libre ou à remplacer
        int freeFrameIndex = -1; // indice de la première frame libre trouvée
        int minAccessCpt = Integer.MAX_VALUE; // cpt d'accès minimum pour la politique LFU
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (pageIds[i] == null) {
                // frame libre trouvée
                freeFrameIndex = i;
                break;
            } else if (accessCpt[i] < minAccessCpt) {
                minAccessCpt = accessCpt[i];
                freeFrameIndex = i;
            }
        }

        // si toutes les frames sont occupées, on fait un remplacement LFU
        if (freeFrameIndex == -1) {
            freeFrameIndex = findFrameToReplace();
            // on écrit la page existante dans cette frame si elle est dirty
            if (flagDirty[freeFrameIndex]) {
                // écrire la page
                try {
                    DiskManager.getInstance().WritePage(pageIds[freeFrameIndex], buffers[freeFrameIndex]);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        // lire la nouvelle page dans la frame sélectionnée
        try {
            DiskManager.getInstance().ReadPage(pageId, buffers[freeFrameIndex]);
        } catch (IOException e) {
            e.printStackTrace();

        }

        pageIds[freeFrameIndex] = pageId;
        pinCounts[freeFrameIndex] = 1;
        flagDirty[freeFrameIndex] = false;
        accessCpt[freeFrameIndex] = 1;

        return buffers[freeFrameIndex];
    }

    // méthode pour trouver la frame à remplacer selon la politique LFU
    private int findFrameToReplace() {
        int minAccessCpt = Integer.MAX_VALUE; // init du compteur d'accès minimal
        int frameToReplace = 0; // init de l'indice de la frame à remplacer

        // parcourir toutes les frames pour trouver celle avec le compteur d'accès le plus bas
        for ( int i = 0 ; i < DBParams.frameCount ; i++ ) {
            if (accessCpt[i] < minAccessCpt) { // si le compteur d'accès de cette frame est plus bas que le min actuel
                minAccessCpt = accessCpt[i]; // on met à jour le compteur d'accès minimal
                frameToReplace = i; // on met à jour l'indice de la frame à remplacer
            }
        }
        return frameToReplace; // on renvoie l'indice de la frame à remplacer
    }


}
