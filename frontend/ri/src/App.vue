<template>
  <v-app style="background-color: #e8f5e9">
    <v-app-bar app color="green" flat dark>
      <v-text-field
        class="mt-7 mx-3"
        light
        solo
        v-model="searchText"
        label="Buscar termino"
        append-icon="mdi-magnify"
        @keydown.enter="searchTerm"
      ></v-text-field>
    </v-app-bar>
    <v-navigation-drawer app class="pa-8">
      <h1>RI Search</h1>
      <v-list>
        <v-subheader class="pl-0">Buscar por Atributo</v-subheader>
        <span>Titulo</span>
        <v-text-field
          v-model="titleFilter"
          filled
          rounded
          single-line
          dense
          @blur="searchTerm"
          label="Titulo"
        ></v-text-field>
        <span>Autor</span>
        <v-text-field
          filled
          rounded
          single-line
          dense
          label="Autor"
        ></v-text-field>
        <span>Contenido</span>
        <v-text-field
          filled
          rounded
          single-line
          dense
          label="Contenido"
        ></v-text-field>
      </v-list>

      <v-subheader class="pl-0">Facetas</v-subheader>
      <v-btn depressed block>Quitar Facetas</v-btn>
      <v-radio-group>
        <v-radio label="Machine Learning"></v-radio>
      </v-radio-group>
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
        <a
          class="mr-1"
          v-for="(author, j) in doc.authors"
          :key="j"
          :href="'https://www.google.com/search?q=' + author"
          target="_blank"
          >{{ author }}</a
        >
        <p>{{ doc.content.substr(0, 255) }}...</p>
        <v-chip
          class="mr-1 mb-1"
          v-for="keyword in doc.keywords"
          :key="keyword"
          @click="selectKeyword(keyword)"
          >{{ keyword }}</v-chip
        >
        <div class="mt-2">{{ doc.year }}</div>
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
    titleFilter: "",
    docs: [],
  }),

  methods: {
    async searchTerm() {
      const { data } = await axios.get(
        `http://localhost:7030/${this.searchText}`,
        {
          params: {
            title: this.titleFilter.length != 0 ? this.titleFilter : null,
          },
        }
      );

      console.log(data);
      this.docs = data;
    },
    selectKeyword(keyword) {
      this.searchText = keyword;
      this.searchTerm();
    },
  },
};
</script>
