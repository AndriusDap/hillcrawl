<template>
  <div id="app" class="tall">
    <el-container class="tall">
      <el-aside class="tall e-padding bg-light">
        <div>Lets ride</div>
      </el-aside>
      <el-main class="tall">
        <Map />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Map from './components/Map.vue'

export default {
  name: 'app',
  components: {
    Map
  }
}
</script>

<style>
html, body {
  margin: 0;
  height: 100%;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.bg-light {
  background: #e5e7e5;
}

.tall {
  height: 100%;
}

.e-padding {
  padding: 20px;
}
</style>
