import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ServicioDeProveedor e2e test', () => {
  const servicioDeProveedorPageUrl = '/servicio-de-proveedor';
  let username: string;
  let password: string;
  const servicioDeProveedorSample = { nombre: 'annually lobster geez', activo: true };

  let servicioDeProveedor;
  let proveedor;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/proveedors',
      body: {
        nombre: 'so',
        tipo: 'OTRO',
        zonaHoraria: 'wherever beneath',
        telefonoContacto: 'astride likewise',
        emailContacto: 'instead pastel',
        urlSoporte: 'cod',
        urlEstado: 'indeed which',
        cobertura: 'VEINTICUATRO_SIETE',
        slaRespuestaMinutos: 335,
        activo: true,
      },
    }).then(({ body }) => {
      proveedor = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/servicio-de-proveedors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/servicio-de-proveedors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/servicio-de-proveedors/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/proveedors', {
      statusCode: 200,
      body: [proveedor],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/incidente-de-proveedors', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (servicioDeProveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicio-de-proveedors/${servicioDeProveedor.id}`,
      }).then(() => {
        servicioDeProveedor = undefined;
      });
    }
  });

  afterEach(() => {
    if (proveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/proveedors/${proveedor.id}`,
      }).then(() => {
        proveedor = undefined;
      });
    }
  });

  it('ServicioDeProveedors menu should load ServicioDeProveedors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('servicio-de-proveedor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServicioDeProveedor').should('exist');
    cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
  });

  describe('ServicioDeProveedor page', () => {
    it('should have translated page title', () => {
      cy.visit(servicioDeProveedorPageUrl);
      cy.getEntityHeading('ServicioDeProveedor').should('not.contain', 'oncallApp.servicioDeProveedor.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(servicioDeProveedorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServicioDeProveedor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${servicioDeProveedorPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ServicioDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/servicio-de-proveedors',
          body: {
            ...servicioDeProveedorSample,
            proveedor,
          },
        }).then(({ body }) => {
          servicioDeProveedor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/servicio-de-proveedors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/servicio-de-proveedors?page=0&size=20>; rel="last",<http://localhost/api/servicio-de-proveedors?page=0&size=20>; rel="first"',
              },
              body: [servicioDeProveedor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(servicioDeProveedorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServicioDeProveedor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('servicioDeProveedor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
      });

      it('edit button click should load edit ServicioDeProveedor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
      });

      it('edit button click should load edit ServicioDeProveedor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioDeProveedor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
      });

      it('last delete button click should delete instance of ServicioDeProveedor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('servicioDeProveedor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioDeProveedorPageUrl);

        servicioDeProveedor = undefined;
      });
    });
  });

  describe('new ServicioDeProveedor page', () => {
    beforeEach(() => {
      cy.visit(servicioDeProveedorPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServicioDeProveedor');
    });

    it('should create an instance of ServicioDeProveedor', () => {
      cy.get(`[data-cy="nombre"]`).type('fooey');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'fooey');

      cy.get(`[data-cy="identificadorExterno"]`).type('through');
      cy.get(`[data-cy="identificadorExterno"]`).should('have.value', 'through');

      cy.get(`[data-cy="descripcion"]`).type('across who');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'across who');

      cy.get(`[data-cy="slaRespuestaMinutos"]`).type('5801');
      cy.get(`[data-cy="slaRespuestaMinutos"]`).should('have.value', '5801');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(`[data-cy="proveedor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        servicioDeProveedor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', servicioDeProveedorPageUrl);
    });
  });
});
