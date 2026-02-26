package com.agri.agrimanager.feign;

import com.agri.agrimanager.dto.PolygonRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "agromonitoring",
        configuration = FeignConfig.class,
        url = "${agromonitoring.base.url}"
)
public interface AgromonitoringClient {
    @PostMapping("/polygons")
    String createPolygon(@RequestBody PolygonRequest polygonRequest);

    @GetMapping("/image/search")
    String serchImages(
            @RequestParam("start") long start,
            @RequestParam("end") long end,
            @RequestParam("polyid") String polyId
            );
    @GetMapping("/ndvi/history")
    String getNdviHistory(
            @RequestParam("start") long start,
            @RequestParam("end") long end,
            @RequestParam("polyid") String polyId
    );
}
