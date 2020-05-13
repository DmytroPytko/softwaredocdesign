import { Moment } from 'moment';
import { IResource } from 'app/shared/model/resource.model';
import { IClient } from 'app/shared/model/client.model';

export interface IProject {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  isActive?: boolean;
  resources?: IResource[];
  client?: IClient;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public isActive?: boolean,
    public resources?: IResource[],
    public client?: IClient
  ) {
    this.isActive = this.isActive || false;
  }
}
