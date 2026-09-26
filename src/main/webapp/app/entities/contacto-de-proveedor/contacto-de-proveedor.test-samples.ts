import { IContactoDeProveedor, NewContactoDeProveedor } from './contacto-de-proveedor.model';

export const sampleWithRequiredData: IContactoDeProveedor = {
  id: 2478,
  nombre: 'sate across how',
  email: 'Carlota50@hotmail.com',
  activo: true,
};

export const sampleWithPartialData: IContactoDeProveedor = {
  id: 30853,
  nombre: 'playfully after dishearten',
  email: 'Debora_QuirozMena@hotmail.com',
  telefono: 'breastplate quizzically',
  rol: 'with',
  activo: false,
};

export const sampleWithFullData: IContactoDeProveedor = {
  id: 17145,
  nombre: 'ick whoever',
  email: 'Emilio_PosadaMora23@hotmail.com',
  telefono: 'drat hence',
  rol: 'edge',
  activo: true,
};

export const sampleWithNewData: NewContactoDeProveedor = {
  nombre: 'notwithstanding',
  email: 'Jorge56@gmail.com',
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
