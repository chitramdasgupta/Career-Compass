package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyServiceImpl(companyRepository, companyMapper);
    }

    @Test
    void getAllCompanies_ShouldReturnPageOfCompanyDtos() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Company company1 = new Company();
        Company company2 = new Company();
        Page<Company> companyPage = new PageImpl<>(Arrays.asList(company1, company2));

        CompanyDto dto1 = new CompanyDto();
        CompanyDto dto2 = new CompanyDto();

        when(companyRepository.findAll(pageable)).thenReturn(companyPage);
        when(companyMapper.toDto(company1)).thenReturn(dto1);
        when(companyMapper.toDto(company2)).thenReturn(dto2);

        // Act
        Page<CompanyDto> result = companyService.getAllCompanies(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(companyRepository).findAll(pageable);
        verify(companyMapper, times(2)).toDto(any(Company.class));
    }

    @Test
    void getCompanyById_WhenCompanyExists_ShouldReturnCompanyDto() {
        // Arrange
        Integer id = 1;
        Company company = new Company();
        CompanyDto companyDto = new CompanyDto();

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        // Act
        CompanyDto result = companyService.getCompanyById(id);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(id);
        verify(companyMapper).toDto(company);
    }

    @Test
    void getCompanyById_WhenCompanyDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 1;
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyById(id));
        verify(companyRepository).findById(id);
    }

    @Test
    void getCompanyByUserId_WhenCompanyExists_ShouldReturnCompanyDto() {
        // Arrange
        Integer userId = 1;
        Company company = new Company();
        CompanyDto companyDto = new CompanyDto();

        when(companyRepository.findByUserId(userId)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        // Act
        CompanyDto result = companyService.getCompanyByUserId(userId);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findByUserId(userId);
        verify(companyMapper).toDto(company);
    }

    @Test
    void getCompanyByUserId_WhenCompanyDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer userId = 1;
        when(companyRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyByUserId(userId));
        verify(companyRepository).findByUserId(userId);
    }

    @Test
    void createCompany_ShouldCreateAndReturnCompanyDto() {
        // Arrange
        User user = new User();
        String name = "Test Company";
        Company company = new Company().setUser(user).setName(name);
        CompanyDto companyDto = new CompanyDto();

        when(companyRepository.save(any(Company.class))).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        // Act
        CompanyDto result = companyService.createCompany(user, name);

        // Assert
        assertNotNull(result);
        verify(companyRepository).save(any(Company.class));
        verify(companyMapper).toDto(company);
    }
}
