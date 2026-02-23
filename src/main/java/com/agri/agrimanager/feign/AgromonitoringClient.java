package com.agri.agrimanager.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "agromonitoring",
        configuration = FeignConfig.class,
        url = "http://api.agromonitoring.com/agro/1.0"
)
public interface AgromonitoringClient {
    @PostMapping("/polygons")
    String createPolygon(@RequestBody String name,String geoJson );

    @GetMapping("images/search")
    String serchImage(
            @RequestParam("start") long start,
            @RequestParam("end") long end,
            @RequestParam("polyid") String polyid
            );
    @GetMapping("/ndvi/history")
    String getNdviHistory(
            @RequestParam("start") long start,
            @RequestParam("end") long end,
            @RequestParam("polyId") String polyId
    );
}
