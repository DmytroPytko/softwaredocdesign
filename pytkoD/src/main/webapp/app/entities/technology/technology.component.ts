import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITechnology } from 'app/shared/model/technology.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TechnologyService } from './technology.service';
import { TechnologyDeleteDialogComponent } from './technology-delete-dialog.component';

@Component({
  selector: 'jhi-technology',
  templateUrl: './technology.component.html'
})
export class TechnologyComponent implements OnInit, OnDestroy {
  technologies: ITechnology[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected technologyService: TechnologyService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.technologies = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.technologyService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITechnology[]>) => this.paginateTechnologies(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.technologies = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTechnologies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITechnology): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTechnologies(): void {
    this.eventSubscriber = this.eventManager.subscribe('technologyListModification', () => this.reset());
  }

  delete(technology: ITechnology): void {
    const modalRef = this.modalService.open(TechnologyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.technology = technology;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTechnologies(data: ITechnology[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.technologies.push(data[i]);
      }
    }
  }
}
