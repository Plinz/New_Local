import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseDone } from 'app/shared/model/purchase-done.model';
import { PurchaseDoneService } from './purchase-done.service';

@Component({
    selector: 'jhi-purchase-done-delete-dialog',
    templateUrl: './purchase-done-delete-dialog.component.html'
})
export class PurchaseDoneDeleteDialogComponent {
    purchaseDone: IPurchaseDone;

    constructor(
        private purchaseDoneService: PurchaseDoneService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseDoneService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseDoneListModification',
                content: 'Deleted an purchaseDone'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-done-delete-popup',
    template: ''
})
export class PurchaseDoneDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseDone }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PurchaseDoneDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.purchaseDone = purchaseDone;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
