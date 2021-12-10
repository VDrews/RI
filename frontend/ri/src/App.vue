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
    <v-navigation-drawer app class="pa-5">
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
          clearable
          clear-icon="mdi-close"
          @blur="searchTerm"
          @keydown.enter="searchTerm"
          label="Titulo"
        ></v-text-field>
        <span>Autor</span>
        <v-text-field
          v-model="authorFilter"
          filled
          rounded
          single-line
          dense
          label="Autor"
          clearable
          clear-icon="mdi-close"
          @keydown.enter="searchTerm"
          @blur="searchTerm"
        ></v-text-field>
        <v-layout justify-space-between align-center>
          <v-subheader class="pl-0">Año de publicación</v-subheader>
          <v-checkbox
            v-model="filterByYear"
            color="green"
            @change="searchTerm"
          ></v-checkbox>
        </v-layout>

        <v-slider
          v-model="yearSelected"
          color="green"
          ticks
          step="1"
          thumb-label="always"
          :min="minYear"
          :max="maxYear"
          class="mx-2 mt-6"
          :disabled="!filterByYear"
          @change="searchTerm"
        ></v-slider>
      </v-list>

      <v-subheader class="pl-0">Facetas</v-subheader>
      <v-card
        v-if="keywordSelected"
        class="py-2 pl-3 pr-1"
        color="green lighten-5"
        rounded
        flat
      >
        <v-layout align-center justify-space-between>
          <span>{{ keywordSelected }}</span>
          <v-btn icon @click="selectKeyword(null)">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-layout>
      </v-card>
      <v-radio-group v-model="keywordSelected" @change="searchTerm">
        <v-radio
          v-for="(keyword, i) in keywords"
          :key="i"
          :value="keyword.label"
          color="green"
        >
          <template v-slot:label>
            <v-layout justify-space-between>
              <span>{{ keyword.label }}</span>
              <span>{{ keyword.value }}</span>
            </v-layout>
          </template>
        </v-radio>
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
          @click="selectAuthor(author)"
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
    authorFilter: "",
    filterByYear: false,
    keywordSelected: null,
    keywords: [],
    years: [],
    docs: [],
    maxYear: 0,
    minYear: 99999,
    yearSelected: 2021,
  }),

  methods: {
    async searchTerm() {
      console.log(this.filterByYear, this.yearSelected);
      const { data } = await axios.get(
        `http://localhost:7030/${this.searchText}`,
        {
          params: {
            title:
              this.titleFilter && this.titleFilter.length != 0
                ? this.titleFilter
                : null,
            author:
              this.authorFilter && this.authorFilter.length != 0
                ? this.authorFilter
                : null,
            year: this.filterByYear ? this.yearSelected : null,
            keyword: this.keywordSelected ? this.keywordSelected : null,
          },
        }
      );

      console.log(data);
      this.docs = data.docs;
      this.keywords = data.keyword;
      this.years = data.year;

      this.years.forEach((year) => {
        this.minYear =
          this.minYear < parseInt(year.label)
            ? this.minYear
            : parseInt(year.label);
        this.maxYear =
          this.maxYear > parseInt(year.label)
            ? this.maxYear
            : parseInt(year.label);
      });

      console.log(this.minYear, this.maxYear);
    },
    selectKeyword(keyword) {
      this.keywordSelected = keyword;
      this.searchTerm();
    },
    selectAuthor(author) {
      this.authorFilter = author;
      this.searchTerm();
    },
  },
};
</script>
