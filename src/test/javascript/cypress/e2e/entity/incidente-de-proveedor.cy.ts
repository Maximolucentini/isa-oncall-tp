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

describe('IncidenteDeProveedor e2e test', () => {
  const incidenteDeProveedorPageUrl = '/incidente-de-proveedor';
  let username: string;
  let password: string;
  // const incidenteDeProveedorSample = {"estado":"ESPERANDO_PROVEEDOR","abiertoEn":"2023-12-04T20:22:03.705Z"};

  let incidenteDeProveedor;
  // let servicioDeProveedor;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicio-de-proveedors',
      body: {"nombre":"muscat what","identificadorExterno":"crystallize","descripcion":"difficult victorious though","slaRespuestaMinutos":7265,"activo":false},
    }).then(({ body }) => {
      servicioDeProveedor = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/incidente-de-proveedors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/incidente-de-proveedors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/incidente-de-proveedors/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/servicio-de-proveedors', {
      statusCode: 200,
      body: [servicioDeProveedor],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (incidenteDeProveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidente-de-proveedors/${incidenteDeProveedor.id}`,
      }).then(() => {
        incidenteDeProveedor = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('IncidenteDeProveedors menu should load IncidenteDeProveedors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('incidente-de-proveedor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IncidenteDeProveedor').should('exist');
    cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
  });

  describe('IncidenteDeProveedor page', () => {
    it('should have translated page title', () => {
      cy.visit(incidenteDeProveedorPageUrl);
      cy.getEntityHeading('IncidenteDeProveedor').should('not.contain', 'oncallApp.incidenteDeProveedor.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(incidenteDeProveedorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IncidenteDeProveedor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${incidenteDeProveedorPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('IncidenteDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/incidente-de-proveedors',
          body: {
            ...incidenteDeProveedorSample,
            servicioDeProveedor: servicioDeProveedor,
          },
        }).then(({ body }) => {
          incidenteDeProveedor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/incidente-de-proveedors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/incidente-de-proveedors?page=0&size=20>; rel="last",<http://localhost/api/incidente-de-proveedors?page=0&size=20>; rel="first"',
              },
              body: [incidenteDeProveedor],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(incidenteDeProveedorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(incidenteDeProveedorPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details IncidenteDeProveedor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('incidenteDeProveedor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
      });

      it('edit button click should load edit IncidenteDeProveedor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncidenteDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
      });

      it('edit button click should load edit IncidenteDeProveedor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncidenteDeProveedor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of IncidenteDeProveedor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('incidenteDeProveedor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);

        incidenteDeProveedor = undefined;
      });
    });
  });

  describe('new IncidenteDeProveedor page', () => {
    beforeEach(() => {
      cy.visit(incidenteDeProveedorPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IncidenteDeProveedor');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of IncidenteDeProveedor', () => {
      cy.get(`[data-cy="ticketExterno"]`).type('precedent');
      cy.get(`[data-cy="ticketExterno"]`).should('have.value', 'precedent');

      cy.get(`[data-cy="estado"]`).select('RECHAZADO');

      cy.get(`[data-cy="abiertoEn"]`).type('2023-12-03T22:59');
      cy.get(`[data-cy="abiertoEn"]`).blur();
      cy.get(`[data-cy="abiertoEn"]`).should('have.value', '2023-12-03T22:59');

      cy.get(`[data-cy="primeraRespuestaEn"]`).type('2023-12-04T10:14');
      cy.get(`[data-cy="primeraRespuestaEn"]`).blur();
      cy.get(`[data-cy="primeraRespuestaEn"]`).should('have.value', '2023-12-04T10:14');

      cy.get(`[data-cy="resueltoEn"]`).type('2023-12-04T03:29');
      cy.get(`[data-cy="resueltoEn"]`).blur();
      cy.get(`[data-cy="resueltoEn"]`).should('have.value', '2023-12-04T03:29');

      cy.get(`[data-cy="cumplioSla"]`).should('not.be.checked');
      cy.get(`[data-cy="cumplioSla"]`).click();
      cy.get(`[data-cy="cumplioSla"]`).should('be.checked');

      cy.get(`[data-cy="motivoRechazo"]`).type('valiantly');
      cy.get(`[data-cy="motivoRechazo"]`).should('have.value', 'valiantly');

      cy.get(`[data-cy="responsableResuelto"]`).type('quaintly glisten');
      cy.get(`[data-cy="responsableResuelto"]`).should('have.value', 'quaintly glisten');

      cy.get(`[data-cy="nivelResuelto"]`).select('PRIMARIO');

      cy.get(`[data-cy="huboCobertura"]`).should('not.be.checked');
      cy.get(`[data-cy="huboCobertura"]`).click();
      cy.get(`[data-cy="huboCobertura"]`).should('be.checked');

      cy.get(`[data-cy="notas"]`).type('growing about whoever');
      cy.get(`[data-cy="notas"]`).should('have.value', 'growing about whoever');

      cy.get(`[data-cy="servicioDeProveedor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        incidenteDeProveedor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', incidenteDeProveedorPageUrl);
    });
  });
});
