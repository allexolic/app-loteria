import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useState } from "react";
import { novoJogoFavorito, listarJogosFavoritos, verificarJogoFavorito } from "../api/favoritos";
import { Alert, Box, Button, Paper, Typography } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import FavoritosTable from "../components/FavoritosTable";
import AddJogoFavoritoModal from "../components/AddJogoFavoritoModal";

function FavoritosPage() {
    const qc = useQueryClient();

    const [open, setOpen] = useState(false);
    const [recalcId, setRecalcId] = useState<number | null>(null);

    const q = useQuery({
        queryKey: ["favoritos"],
        queryFn: listarJogosFavoritos,
    });

    const mNew = useMutation({
        mutationFn: (bolas: number[]) => novoJogoFavorito(bolas),
        onSuccess: async () => {
            setOpen(false);
            await qc.invalidateQueries({ queryKey: ["favoritos"] });
        },
    });

    const mCheck = useMutation({
        mutationFn: (id: number) => verificarJogoFavorito(id),
        onMutate: (id) => setRecalcId(id),
        onSettled: () => setRecalcId(null),
        onSuccess: async () => {
            await qc.invalidateQueries({ queryKey: ["favoritos"] });
        },
    });

    return (
        <Box>
            <Paper
                sx={{
                    p: { xs: 2, sm: 2.5 },
                    mb: 2,
                    display: "flex",
                    alignItems: { xs: "stretch", sm: "center" },
                    flexDirection: { xs: "column", sm: "row" },
                    gap: 1.5,
                }}>
                <Box sx={{ flexGrow: 1 }}>
                    <Typography variant="h5" fontWeight={800}>Jogos favoritos</Typography>
                    <Typography variant="body2" color="text.secondary">
                        Adicione seus jogos favoritos para conferir os resultados
                    </Typography>
                </Box>
                <Box sx={{ alignSelf: { xs: "stretch", sm: "auto" }, ml: { sm: "auto" } }}>
                    <Button variant="contained" startIcon={<AddIcon />} onClick={() => setOpen(true)}>
                        Adicionar
                    </Button>
                </Box>
            </Paper>
            {mNew.isError && <Alert severity="error">Falha ao cadastrar jogo favorito.</Alert>}
            {mCheck.isError && <Alert severity="error">Falha ao verificar jogo.</Alert>}

            <Paper>
                <FavoritosTable data={q.data ?? []} onRecalcular={(id) => mCheck.mutate(id)} recalculandoId={recalcId} />
                {q.isLoading && <Box sx={{ p: 2 }}>Carregando...</Box>}    
            </Paper>
            
            <AddJogoFavoritoModal open={open} onClose={() => setOpen(false)} loading={mNew.isPending} onSubmit={(bolas) => mNew.mutate(bolas)}/>
        </Box>
    )
}

export default FavoritosPage