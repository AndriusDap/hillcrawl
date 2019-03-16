<template>
  <MglMap
    id="map-wrapper"
    container="main-map"
    :center.sync="center"
    :accessToken="accessToken"
    :mapStyle="mapStyle"
    :zoom="zoom"
  >
  </MglMap>
</template>

<script>
import {
  MglMap,
  MglMarker,
  MglGeojsonLayer
} from 'vue-mapbox'

export default {
  components: {
    MglMap,
    MglMarker,
    MglGeojsonLayer
  },
  data() {
    return {
      accessToken: '',
      mapStyle: 'mapbox://styles/mapbox/light-v9',
      layerId: 'firstLayer',
      sourceId: 'firstSource',
      center: [23, 54.8],
      zoom: 7
    }
  }
}
</script>
<style scoped>
#map-wrapper {
  /*position: absolute;*/
}

#main-map {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* position: absolute;*/
  background: red;
}
</style>