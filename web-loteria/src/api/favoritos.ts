import { http } from "./http";

export type JogoFavorito =  {
    id: number;
    balls: number[];
    awardWinningGame: boolean;
    totalNumbersAwardsWinning: number;
};

export async function novoJogoFavorito(balls: number[]) {
    const { data } = await http.post<JogoFavorito>("/api/jogos/favoritos", { balls });
    return data;
}

export async function verificarJogoFavorito(gameId: number) {
    const { data } = await http.post<JogoFavorito>("/api/jogos/recalcular", {gameId });
    return data;
}

export async function listarJogosFavoritos(): Promise<JogoFavorito[]> {
    const { data } = await http.get("/api/jogos/favoritos");
    return data;
}