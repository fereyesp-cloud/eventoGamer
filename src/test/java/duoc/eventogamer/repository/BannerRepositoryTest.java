package duoc.eventogamer.repository;

import duoc.eventogamer.model.Banner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BannerRepositoryTest {

    @Test
    @DisplayName("findByActivoTrueOrderByOrdenAsc retorna los banners activos ordenados")
    void findByActivoTrueOrderByOrdenAsc() {
        BannerRepository repo = mock(BannerRepository.class);
        when(repo.findByActivoTrueOrderByOrdenAsc()).thenReturn(List.of(new Banner("Banner 1", 1)));

        List<Banner> activos = repo.findByActivoTrueOrderByOrdenAsc();
        assertEquals(1, activos.size());
        verify(repo).findByActivoTrueOrderByOrdenAsc();
    }
}
