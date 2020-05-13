import { IProject } from 'app/shared/model/project.model';

export interface IClient {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phone?: string;
  address?: string;
  projects?: IProject[];
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public phone?: string,
    public address?: string,
    public projects?: IProject[]
  ) {}
}
