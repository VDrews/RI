<template>
  <v-app style="background-color: #e8f5e9">
    <v-app-bar app color="green" flat dark>
      <v-text-field
        class="mt-7 mx-3"
        light
        solo
        v-model="searchText"
        label="Buscar termino"
        @keydown.enter="searchTerm"
      ></v-text-field>
    </v-app-bar>
    <v-navigation-drawer app class="pa-8">
      <h1>RI Search</h1>
      <v-list>
        <v-subheader class="pl-0">Buscar por Atributo</v-subheader>
        <v-text-field filled rounded label="Titulo"></v-text-field>
        <v-text-field filled rounded label="Autor"></v-text-field>
        <v-text-field filled rounded label="Contenido"></v-text-field>
      </v-list>
    </v-navigation-drawer>
    <v-content>
      <div
        v-if="docs.length > 0"
        class="mt-4 ml-4 green--text font-weight-bold"
      >
        Mostrando {{ docs.length }} Resultados
      </div>
      <v-card
        v-for="(doc, i) in docs"
        :key="i"
        class="pa-4 ma-4 rounded-lg"
        outlined
      >
        <h3>{{ doc.title }}</h3>
        <p>{{ doc.content.substr(0, 255) }}...</p>
        <span>{{ doc.year }}</span>
      </v-card>
    </v-content>
  </v-app>
</template>

<script>
import axios from "axios";
export default {
  name: "App",

  data: () => ({
    searchText: "",
    docs: [],
  }),

  methods: {
    async searchTerm() {
      const { data } = await axios.get(
        `http://localhost:7030/${this.searchText}`
      );

      console.log(data);
      this.docs = data;
    },
  },
};
</script>
