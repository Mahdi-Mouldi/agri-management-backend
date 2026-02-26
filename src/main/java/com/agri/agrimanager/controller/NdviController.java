package com.agri.agrimanager.controller;

import com.agri.agrimanager.entity.NdviImage;
import com.agri.agrimanager.service.NdviService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ndvi")
@RequiredArgsConstructor
@CrossOrigin
public class NdviController {
    private final NdviService ndviService;

    // 1) Créer polygon côté Agromonitoring
    // POST /api/ndvi/polygon/{parcelleId}
    @PostMapping("/polygon/{parcelleId}")
    public ResponseEntity<String> createPolygon(@PathVariable Long parcelleId) throws Exception{
        try{
            String polygonId = ndviService.createPolygon(parcelleId);
            return ResponseEntity.ok(polygonId);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────
    // 2) Chercher images (JSON brut Agromonitoring)
    // GET /api/ndvi/search/{parcelleId}?startDate=2026-01-01&endDate=2026-02-01
    // ─────────────────────────────────────────────────────

    @GetMapping("/search/{parcelleId}")
    public ResponseEntity<String> searchNdviImages(@PathVariable Long parcelleId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate ){
        try{
            String result = ndviService.searchNdviImages(parcelleId, startDate, endDate);
            return ResponseEntity.ok(result);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────
    // 3) Chercher historique NDVI (JSON brut Agromonitoring)
    // GET /api/ndvi/history/{parcelleId}?startDate=2026-01-01&endDate=2026-02-01
    // ─────────────────────────────────────────────────────
    @GetMapping ("/history/{parcelleId}")
    public ResponseEntity<String> searchNdviHistory(@PathVariable Long parcelleId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        try{
            String result = ndviService.searchNdviHistory(parcelleId, startDate, endDate);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    // ─────────────────────────────────────────────────────
    // 4) SYNC: récupérer + sauvegarder en BD
    // POST /api/ndvi/sync/{parcelleId}?startDate=2026-01-01&endDate=2026-02-01
    // ─────────────────────────────────────────────────────
    @PostMapping("/sync/{parcelleId}")
    public ResponseEntity<List<NdviImage>> syncNdviImages(
            @PathVariable Long parcelleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<NdviImage> images = ndviService.syncNdviImages(parcelleId, startDate, endDate);
            return ResponseEntity.ok(images);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
// ─────────────────────────────────────────────────────
    // 5) Récupérer une image NDVI par son ID
    // GET /api/ndvi/image/{id}
    // ─────────────────────────────────────────────────────
    @GetMapping("/image/{id}")
    public ResponseEntity<NdviImage> getNdviImageById(@PathVariable Long id) {
        return ndviService.getNdviImageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/parcelle/{parcelleId}")

    public void deleteImageByParcelle(@PathVariable Long parcelleId){
        ndviService.deleteNdviImageByParcelleId(parcelleId);
    }


}
