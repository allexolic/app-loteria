import { AppBar, Box, Button, Container, Toolbar, Typography } from '@mui/material'
import { Link, Navigate, Route, Routes } from 'react-router-dom'
import ArquivosPage from './pages/ArquivosPage'
import FavoritosPage from './pages/FavoritosPage'

function App() {

  return (
    <>
      <Box sx={{ minHeight: "100dvh", bgcolor: "background.default" }}>
        <AppBar
          position="sticky"
          elevation={0}
          sx={{
            top: 0,
            zIndex: (t) => t.zIndex.drawer + 1,
            borderBottom: "1px solid",
            borderColor: "divider",
            bgcolor: "background.paper",
            color: "text.primary",
            backdropFilter: "blur(10px)",
          }}
        >
          <Toolbar>
            <Typography
              variant='h6'
              sx={{
                flexGrow: 1,
                fontWeight: 800,
                letterSpacing: -0.3,
                whiteSpace: "nowrap",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}
              title="Lotofácil - Resultados & Jogos favoritos"
            >
              Lotofácil - Resultados & Jogos favoritos
            </Typography>
            <Button color='inherit' component={Link} to="/arquivos" sx={{ borderRadius: 999, px: 2 }}>
              Arquivos
            </Button>
            <Button color='inherit' component={Link} to="/favoritos" sx={{ borderRadius: 999, px: 2 }}>
              Favoritos
            </Button>
          </Toolbar>
        </AppBar>

        <Container sx={{ py: { xs: 2, sm: 3 } }}>
          <Routes>
            <Route path="/" element={<Navigate to="/arquivos" replace />} />
            <Route path="/arquivos" element={<ArquivosPage />}/>
            <Route path="/favoritos" element={<FavoritosPage />}/>
          </Routes>
        </Container>
      </Box>
    </>
  )
}

export default App
