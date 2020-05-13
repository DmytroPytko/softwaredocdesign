import { IResource } from 'app/shared/model/resource.model';
import { ITechnology } from 'app/shared/model/technology.model';

export interface IEmployee {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phone?: string;
  address?: string;
  resources?: IResource[];
  technologies?: ITechnology[];
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public phone?: string,
    public address?: string,
    public resources?: IResource[],
    public technologies?: ITechnology[]
  ) {}
}
