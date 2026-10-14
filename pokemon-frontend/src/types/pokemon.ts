export interface Pokemon {
    id: number;
    name: string;
    imageUrl: string;
    weight: number;
    abilities: string[];
  }
  
  export interface PokemonPage {
    content: Pokemon[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
  }