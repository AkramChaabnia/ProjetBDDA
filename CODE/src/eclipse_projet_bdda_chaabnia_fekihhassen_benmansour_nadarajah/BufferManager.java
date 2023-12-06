package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;


import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferManager {
    private static BufferManager instance = new BufferManager();

    // Informations associées à chaque frame
    private ByteBuffer[] buffers;
    private PageId[] pageIds;
    private int[] pinCounts;
    private boolean[] flagDirty;
    private int[] accessCpt;

    public static BufferManager getInstance() {
        return instance;
    }

    /**
     * Lorsqu'une page est demandée, on recherche si la page est déjà dans le buffer,
     * si elle l'est, on incrémente le pinCount associé à la frame correspondante.
     * Si la page n'est pas dans le buffer, on suit une procédure pour trouver une
     * frame libre ou à remplacer.
     * On charge ensuite la nouvelle page dans la frame choisie et ajuste le
     * pinCount et le dirty en conséquence.
     * 
     * @param pageId
     * @return
     */
    public ByteBuffer getPage(PageId pageId) throws IOException {
        // On recherche si la page est déjà dans le buffer
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (pageIds[i] != null && pageIds[i].equals(pageId)) {
                // Si la page est trouvée dans le buffer, mettre à jour le compteur d'accès
                accessCpt[i]++;
                return buffers[i]; // On retourne le buffer correspondant
            }
        }

        // Si la page n'est pas dans le buffer, on cherche une frame libre ou à remplacer
        int freeFrameIndex = -1; // Indice de la première frame libre trouvée
        int minAccessCpt = Integer.MAX_VALUE; // Compteur d'accès minimum pour la politique LFU
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (pageIds[i] == null) {
                // Frame libre trouvée
                freeFrameIndex = i;
                break;
            } else if (accessCpt[i] < minAccessCpt) {
                minAccessCpt = accessCpt[i];
                freeFrameIndex = i;
            }
        }

        // Si toutes les frames sont occupées, on effectue un remplacement LFU
        if (freeFrameIndex == -1) {
            freeFrameIndex = findFrameToReplace();
            // On écrit la page existante dans cette frame si elle est dirty
            if (flagDirty[freeFrameIndex]) {
                // Écrire la page
                DiskManager.getInstance().writePage(pageIds[freeFrameIndex], buffers[freeFrameIndex]);
            }
        }

        // Lire la nouvelle page dans la frame sélectionnée
        DiskManager.getInstance().readPage(pageId, buffers[freeFrameIndex]);

        pageIds[freeFrameIndex] = pageId;
        pinCounts[freeFrameIndex] = 1;
        flagDirty[freeFrameIndex] = false;
        accessCpt[freeFrameIndex] = 1;

        return buffers[freeFrameIndex];
    }

    // Méthode pour trouver la frame à remplacer selon la politique LFU
    private int findFrameToReplace() {
        int minAccessCpt = Integer.MAX_VALUE; // Initialisation du compteur d'accès minimal
        int frameToReplace = 0; // Initialisation de l'indice de la frame à remplacer

        // Parcourir toutes les frames pour trouver celle avec le compteur d'accès le plus bas
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (accessCpt[i] < minAccessCpt) { // Si le compteur d'accès de cette frame est plus bas que le minimum actuel
                minAccessCpt = accessCpt[i]; // Mise à jour du compteur d'accès minimal
                frameToReplace = i; // Mise à jour de l'indice de la frame à remplacer
            }
        }
        return frameToReplace; // On renvoie l'indice de la frame à remplacer
    }

    /**
     * Lorsqu'on libère une page avec FreePage, on décrémente le pinCount,
     * ce qui permet de suivre le nombre d'utilisations de la frame.
     * On ajuste également le dirty en fonction de la valeur de valDirty.
     * 
     * @param pageId
     * @param valDirty
     * @throws PageNotFoundException
     */
    public void freePage(PageId pageId, boolean valDirty) throws PageNotFoundException {
        // On cherche si la page est déjà dans le buffer
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (pageIds[i] != null && pageIds[i].equals(pageId)) {
                // Page trouvée dans le buffer
                pinCounts[i]--; // Décrémenter le pin_count de la page

                if (valDirty) {
                    // Si valDirty est vrai, définir le flag dirty à true
                    flagDirty[i] = true;
                }

                return;
            }
        }

        // Si la page n'est pas trouvée dans le buffer, on lance une exception personnalisée
        throw new PageNotFoundException("La page avec l'ID " + pageId + " n'a pas été trouvée dans le buffer.");
    }

    /**
     * Dans cette méthode, on parcourt toutes les frames du buffer et on écrit sur le
     * disque toutes les pages qui ont le dirty à 1.
     * Après l'écriture, on remet à zéro les informations associées à chaque frame,
     * y compris pageIds, pinCounts, flagDirty et accessCpt, ce qui prépare les
     * frames pour une nouvelle utilisation.
     */
    public void flushAll() throws IOException {
        // On parcourt toutes les frames du buffer
        for (int i = 0; i < DBParams.frameCount; i++) {
            if (pageIds[i] != null && flagDirty[i]) {
                // Si le flag dirty est à 1 pour cette frame, écrire la page sur le disque
                DiskManager.getInstance().writePage(pageIds[i], buffers[i]);
                // On remet à zéro les informations de la frame
                pageIds[i] = null;
                pinCounts[i] = 0;
                flagDirty[i] = false;
                accessCpt[i] = 0;
            }
        }
    }
    
}