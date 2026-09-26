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

describe('TurnoDeProveedor e2e test', () => {
  const turnoDeProveedorPageUrl = '/turno-de-proveedor';
  let username: string;
  let password: string;
  // const turnoDeProveedorSample = {"desde":"2023-12-04T15:57:56.459Z","hasta":"2023-12-04T14:26:27.626Z","nivel":"SECUNDARIO","esReemplazo":false};

  let turnoDeProveedor;
  // let contactoDeProveedor;

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
      url: '/api/contacto-de-proveedors',
      body: {"nombre":"orientate anti","email":"Rebeca.TovarSierra0@hotmail.com","telefono":"safely","rol":"immaculate witty","activo":false},
    }).then(({ body }) => {
      contactoDeProveedor = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/turno-de-proveedors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/turno-de-proveedors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/turno-de-proveedors/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/contacto-de-proveedors', {
      statusCode: 200,
      body: [contactoDeProveedor],
    });

  });
   */

  afterEach(() => {
    if (turnoDeProveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/turno-de-proveedors/${turnoDeProveedor.id}`,
      }).then(() => {
        turnoDeProveedor = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (contactoDeProveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contacto-de-proveedors/${contactoDeProveedor.id}`,
      }).then(() => {
        contactoDeProveedor = undefined;
      });
    }
  });
   */

  it('TurnoDeProveedors menu should load TurnoDeProveedors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('turno-de-proveedor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TurnoDeProveedor').should('exist');
    cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
  });

  describe('TurnoDeProveedor page', () => {
    it('should have translated page title', () => {
      cy.visit(turnoDeProveedorPageUrl);
      cy.getEntityHeading('TurnoDeProveedor').should('not.contain', 'oncallApp.turnoDeProveedor.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(turnoDeProveedorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TurnoDeProveedor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${turnoDeProveedorPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TurnoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/turno-de-proveedors',
          body: {
            ...turnoDeProveedorSample,
            contacto: contactoDeProveedor,
          },
        }).then(({ body }) => {
          turnoDeProveedor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/turno-de-proveedors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/turno-de-proveedors?page=0&size=20>; rel="last",<http://localhost/api/turno-de-proveedors?page=0&size=20>; rel="first"',
              },
              body: [turnoDeProveedor],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(turnoDeProveedorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(turnoDeProveedorPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TurnoDeProveedor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('turnoDeProveedor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
      });

      it('edit button click should load edit TurnoDeProveedor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TurnoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
      });

      it('edit button click should load edit TurnoDeProveedor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TurnoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of TurnoDeProveedor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('turnoDeProveedor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', turnoDeProveedorPageUrl);

        turnoDeProveedor = undefined;
      });
    });
  });

  describe('new TurnoDeProveedor page', () => {
    beforeEach(() => {
      cy.visit(turnoDeProveedorPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TurnoDeProveedor');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of TurnoDeProveedor', () => {
      cy.get(`[data-cy="desde"]`).type('2023-12-04T16:39');
      cy.get(`[data-cy="desde"]`).blur();
      cy.get(`[data-cy="desde"]`).should('have.value', '2023-12-04T16:39');

      cy.get(`[data-cy="hasta"]`).type('2023-12-04T07:10');
      cy.get(`[data-cy="hasta"]`).blur();
      cy.get(`[data-cy="hasta"]`).should('have.value', '2023-12-04T07:10');

      cy.get(`[data-cy="nivel"]`).select('PRIMARIO');

      cy.get(`[data-cy="esReemplazo"]`).should('not.be.checked');
      cy.get(`[data-cy="esReemplazo"]`).click();
      cy.get(`[data-cy="esReemplazo"]`).should('be.checked');

      cy.get(`[data-cy="nota"]`).type('instead');
      cy.get(`[data-cy="nota"]`).should('have.value', 'instead');

      cy.get(`[data-cy="contacto"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        turnoDeProveedor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', turnoDeProveedorPageUrl);
    });
  });
});
