import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IResource } from 'app/shared/model/resource.model';

type EntityResponseType = HttpResponse<IResource>;
type EntityArrayResponseType = HttpResponse<IResource[]>;

@Injectable({ providedIn: 'root' })
export class ResourceService {
  public resourceUrl = SERVER_API_URL + 'api/resources';

  constructor(protected http: HttpClient) {}

  create(resource: IResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resource);
    return this.http
      .post<IResource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(resource: IResource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resource);
    return this.http
      .put<IResource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IResource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(resource: IResource): IResource {
    const copy: IResource = Object.assign({}, resource, {
      startDate: resource.startDate && resource.startDate.isValid() ? resource.startDate.toJSON() : undefined,
      endDate: resource.endDate && resource.endDate.isValid() ? resource.endDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((resource: IResource) => {
        resource.startDate = resource.startDate ? moment(resource.startDate) : undefined;
        resource.endDate = resource.endDate ? moment(resource.endDate) : undefined;
      });
    }
    return res;
  }
}
